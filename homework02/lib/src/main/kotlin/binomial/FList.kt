package binomial

/*
 * FList - реализация функционального списка
 *
 * Пустому списку соответствует тип Nil, непустому - Cons
 *
 * Запрещено использовать
 *
 *  - var
 *  - циклы
 *  - стандартные коллекции
 *
 *  Исключение Array-параметр в функции flistOf. Но даже в ней нельзя использовать цикл и forEach.
 *  Только обращение по индексу
 */
sealed class FList<T> : Iterable<T> {
    // размер списка, 0 для Nil, количество элементов в цепочке для Cons
    abstract val size: Int
    // пустой ли списк, true для Nil, false для Cons
    abstract val isEmpty: Boolean

    abstract fun headOrNull(): T?

    abstract fun tail(): FList<T>

    // получить список, применив преобразование
    // требуемая сложность - O(n)
    abstract fun <U> map(f: (T) -> U): FList<U>

    // получить список из элементов, для которых f возвращает true
    // требуемая сложность - O(n)
    abstract fun filter(f: (T) -> Boolean): FList<T>

    // свертка
    // требуемая сложность - O(n)
    // Для каждого элемента списка (curr) вызываем f(acc, curr),
    // где acc - это base для начального элемента, или результат вызова
    // f(acc, curr) для предыдущего
    // Результатом fold является результат последнего вызова f(acc, curr)
    // или base, если список пуст
    abstract fun <U> fold(base: U, f: (U, T) -> U): U

    // разворот списка
    // требуемая сложность - O(n)
    fun reverse(): FList<T> = fold<FList<T>>(nil()) { acc, current ->
        Cons(current, acc)
    }

    override fun iterator(): Iterator<T> = FListIterator(this)

    /*
     * Это не очень красиво, что мы заводим отдельный Nil на каждый тип
     * И вообще лучше, чтобы Nil был объектом
     *
     * Но для этого нужны приседания с ковариантностью
     *
     * dummy - костыль для того, что бы все Nil-значения были равны
     *         и чтобы Kotlin-компилятор был счастлив (он требует, чтобы у Data-классов
     *         были свойство)
     *
     * Также для борьбы с бойлерплейтом были введены функция и свойство nil в компаньоне
     */
    data class Nil<T>(private val dummy: Int = 0) : FList<T>() {
        override val size = 0
        override val isEmpty = true

        override fun headOrNull() = null

        override fun tail() = Nil<T>()

        override fun <U> fold(base: U, f: (U, T) -> U) = base

        override fun filter(f: (T) -> Boolean) = Nil<T>()

        override fun <U> map(f: (T) -> U) = Nil<U>()
    }

    data class Cons<T>(val head: T, val tail: FList<T>) : FList<T>() {
        override val size = 1 + tail.size
        override val isEmpty = false

        override fun headOrNull() = head

        override fun tail() = tail

        private tailrec fun <U> foldRecur(ans: U, f: (U, T) -> U, it: FListIterator<T>): U =
            if (!it.hasNext()) ans else foldRecur(f(ans, it.next()), f, it)

        override fun <U> fold(base: U, f: (U, T) -> U) = foldRecur(base, f, FListIterator(this))

        private tailrec fun filterRecur(f: (T) -> Boolean, ans: FList<T>, it: FListIterator<T>): FList<T> =
            if (!it.hasNext()) ans
            else {
                val head = it.next()
                filterRecur(f, if (f(head)) Cons(head, ans) else ans, it)
            }

        override fun filter(f: (T) -> Boolean) = filterRecur(f, Nil(), FListIterator(this))

        private tailrec fun <U> mapRecur(f: (T) -> U, ans: FList<U>, it: FListIterator<T>): FList<U> =
            if (!it.hasNext()) ans else mapRecur(f, Cons(f(it.next()), ans), it)

        override fun <U> map(f: (T) -> U): FList<U> {
            val it = FListIterator(reverse())
            return mapRecur(f, Nil(), it)
        }
    }

    private class FListIterator<T>(var fl: FList<T>) : Iterator<T> {
        override fun hasNext() = !fl.isEmpty

        override fun next(): T {
            if (!hasNext()) {
                throw NoSuchElementException()
            }
            val ans = (fl as Cons).headOrNull()
            fl = fl.tail()
            return ans
        }
    }

    companion object {
        fun <T> nil() = Nil<T>()
        val nil = Nil<Any>()
    }
}

// конструирование функционального списка в порядке следования элементов
// требуемая сложность - O(n)

internal tailrec fun <T> build(ans: FList<T>, it: Iterator<T>): FList<T> =
    if (!it.hasNext()) ans
    else build(FList.Cons(it.next(), ans), it)

fun <T> flistOf(vararg values: T): FList<T> = build(FList.nil(), values.iterator())
