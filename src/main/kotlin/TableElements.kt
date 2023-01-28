
class Table(caption: String = "") : HtmlElement("table", Pair("caption", caption)), TableTag {
    fun tr(init: Tr.() -> Unit) = append(Tr(), init)
}

class Tr : HtmlElement("tr"), TableTag {
    fun td(init: Td.() -> Unit) = append(Td(), init)
}

class Td : ContentElement("td"), TableTag
