import java.io.FileWriter
import Align.*

fun html(init: Html.() -> Unit) = Html().apply(init)

open class HtmlElement(tag: String, vararg prop: Pair<String, String>) {
    private val children = mutableListOf<HtmlElement>()
    protected var openTag = if (tag.isEmpty()) "" else buildString {
        append("<").append(tag)
        prop.filter { it.second.isNotEmpty() }.forEach { append(" ${it.first}=\"${it.second}\"") }
        append(">")
    }
    protected var closeTag = if (tag.isEmpty()) "" else "</$tag>"
    private val countOfThreads = 4

    protected fun <T : HtmlElement> append(child: T, init: T.() -> Unit) {
        child.init()
        children += child
    }

    protected open fun toHtml(sb: StringBuilder, depth: Int, isContent: Boolean = false) {
        val prefix = "\t".repeat(depth)
        sb.append(prefix).append(openTag)
        if (!isContent)
            sb.append("\n")
        children.forEach { it.toHtml(sb, depth + 1) }
        if (!isContent)
            sb.append(prefix)
        sb.append(closeTag).append("\n")
    }
}

class Html : HtmlElement("html"), MainTag {
    fun toHtml() = buildString { toHtml(this, 0) }

    fun head(init: Head.() -> Unit) = append(Head(), init)

    fun body(init: Body.() -> Unit) = append(Body(), init)

    fun saveToFile(name: String) {
        FileWriter(name).use {
            it.write(toHtml())
        }
    }
}
