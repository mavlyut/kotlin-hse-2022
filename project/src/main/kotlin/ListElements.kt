
class UnorderedList : HtmlElement("ul"), ListTag {
    fun li(init: ListItem.() -> Unit) = append(ListItem(), init)
}

class OrderedList : HtmlElement("ol"), ListTag {
    fun li(init: ListItem.() -> Unit) = append(ListItem(), init)
}

class ListItem : ContentElement("li"), ListTag {
    init {
        closeTag = ""
    }
}
