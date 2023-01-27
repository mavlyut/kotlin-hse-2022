package homework03.serialization

import com.soywiz.korio.async.use
import com.soywiz.korio.file.VfsOpenMode
import com.soywiz.korio.file.std.localCurrentDirVfs
import com.soywiz.korio.file.std.localVfs
import com.soywiz.korio.stream.writeString
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun <T : Any> csvSerialize(data: Iterable<T>, klass: KClass<T>) = buildString { serializeObject(data, klass) }

private val primitives = listOf(Integer::class, Short::class, Long::class, Byte::class, Float::class, Double::class)
private val cwd = localCurrentDirVfs.path

private fun <T: Any> StringBuilder.serializeObject(data: Iterable<T>, klass: KClass<T>) {
    serializeHeader(klass)
    if (data.any { it.javaClass.kotlin != klass })
        throw IllegalArgumentException("not all types match")
    data.forEach {
        serializeObject(it)
    }
}

private fun StringBuilder.serializeNumber(value: Number) = apply { append(value) }

private fun StringBuilder.serializeValue(value: Any?) = apply {
    if (value == null) {
        serializeString("null")
        return@apply
    }
    when (value.javaClass.kotlin) {
        String::class -> {
            serializeString(value as String)
        }
        in primitives -> {
            serializeNumber(value as Number)
        }
    }
}

private fun StringBuilder.serializeString(value: String) = apply {
    append('"')
    append(value.replace("\"".toRegex(), "\"\""))
    append('"')
}

private fun <T: Any> StringBuilder.serializeHeader(klass: KClass<T>) = apply {
    when (klass) {
        String::class -> {
            serializeString("value")
        }
        else -> {
            klass.memberProperties.joinTo(this, ",") { p ->
                serializeString(p.name)
                ""
            }
        }
    }
    append("\n")
}

private fun StringBuilder.serializeObject(value: Any) {
    when (val kClass = value.javaClass.kotlin) {
        String::class -> {
            serializeString(value as String)
        }
        else -> {
            kClass.memberProperties.joinTo(this, ",") { p ->
                serializeValue(p.get(value))
                ""
            }
        }
    }
    append("\n")
}

suspend fun <T : Any> serializeAndWrite(data: Iterable<T>, klass: KClass<T>, file: String, path: String = cwd) {
    localVfs(path)[file].open(VfsOpenMode.CREATE).use {
        writeString(csvSerialize(data, klass))
    }
}
