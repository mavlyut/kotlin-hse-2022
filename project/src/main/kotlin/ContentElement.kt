
class ContentData : HtmlElement("") {
    private val content = mutableListOf<String>()

    override fun toHtml(sb: StringBuilder, depth: Int, isContent: Boolean) {
        content.joinTo(sb, separator = "\n", prefix = openTag, postfix = closeTag)
    }

    operator fun String.unaryPlus() = append(ContentData()) { }

    operator fun plusAssign(str: String) {
        content += str
            .replace("&", "&amp ")
            .replace("\"", "&quot ")
            .replace("<", "&lt ")
            .replace(">", "&gt ")
    }
}

open class ContentElement(tag: String, vararg prop: Pair<String, String>) : HtmlElement(tag, *prop) {
    private val textBlock = ContentData()
    init {
        append(textBlock) { }
    }

    operator fun String.unaryPlus() { textBlock += this }

    override fun toHtml(sb: StringBuilder, depth: Int, isContent: Boolean) {
        super.toHtml(sb, depth, true)
    }
}
