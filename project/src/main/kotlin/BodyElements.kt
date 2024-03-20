
class Body : HtmlElement("body"), MainTag {
    fun a(link: String, init: Link.() -> Unit) = append(Link(link), init)
    fun table(init: Table.() -> Unit) = append(Table(), init)
    fun comment(init: Comment.() -> Unit) = append(Comment(), init)
    fun p(align: Align = Align.default, init: TextBlock.() -> Unit) = append(TextBlock(align), init)
    fun div(init: Div.() -> Unit) = append(Div(), init)
    fun ul(init: UnorderedList.() -> Unit) = append(UnorderedList(), init)
    fun ol(init: OrderedList.() -> Unit) = append(OrderedList(), init)
    fun strong(init: Strong.() -> Unit) = append(Strong(), init)
    fun em(init: Emphasis.() -> Unit) = append(Emphasis(), init)
    fun i(init: Italic.() -> Unit) = append(Italic(), init)
    fun b(init: Bold.() -> Unit) = append(Bold(), init)
    fun mark(init: Mark.() -> Unit) = append(Mark(), init)
    fun quote(init: Quote.() -> Unit) = append(Quote(), init)
    fun h1(init: Header1.() -> Unit) = append(Header1(), init)
    fun h2(init: Header2.() -> Unit) = append(Header2(), init)
    fun h3(init: Header3.() -> Unit) = append(Header3(), init)
    fun h4(init: Header4.() -> Unit) = append(Header4(), init)
    fun h5(init: Header5.() -> Unit) = append(Header5(), init)
    fun h6(init: Header6.() -> Unit) = append(Header6(), init)
}

class Div : HtmlElement("div")

class Link(link: String) : ContentElement("a", Pair("href", link))

class Comment : ContentElement("") {
    init {
        openTag = "<!--"
        closeTag = "-->"
    }
}
