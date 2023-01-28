
enum class Align {
    left, right, center, justify, default;

    override fun toString() = if (this == default) "" else this.name
}

class TextBlock(align: Align) : ContentElement("p", Pair("align", align.toString()))

class Strong : ContentElement("strong")
class Emphasis : ContentElement("em")
class Italic : ContentElement("i")
class Bold : ContentElement("b")
class Mark : ContentElement("mark")
class Quote : ContentElement("q")
class Header1 : ContentElement("h1")
class Header2 : ContentElement("h2")
class Header3 : ContentElement("h3")
class Header4 : ContentElement("h4")
class Header5 : ContentElement("h5")
class Header6 : ContentElement("h6")
