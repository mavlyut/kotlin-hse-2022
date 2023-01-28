
class Body : HtmlElement("body"), MainTag {
    fun a(link: String, init: Link.() -> Unit) = append(Link(link), init)

    fun table(init: Table.() -> Unit) = append(Table(), init)

    fun comment(init: Comment.() -> Unit) = append(Comment(), init)

    fun p(init: TextBlock.() -> Unit) = append(TextBlock(), init)

    fun div(init: Div.() -> Unit) = append(Div(), init)

    fun ul(init: UnorderedList.() -> Unit) = append(UnorderedList(), init)

    fun ol(init: OrderedList.() -> Unit) = append(OrderedList(), init)
}

class Div : HtmlElement("div")

class Link(link: String) : ContentElement("a", Pair("href", link))

class Comment : ContentElement("") {
    init {
        openTag = "<!--"
        closeTag = "-->"
    }
}

class TextBlock : ContentElement("p")
