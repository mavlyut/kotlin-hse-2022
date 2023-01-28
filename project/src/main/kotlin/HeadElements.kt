
class Head : HtmlElement("head"), MainTag {
    fun title(init: Title.() -> Unit) = append(Title(), init)
}

class Title : ContentElement("title")
