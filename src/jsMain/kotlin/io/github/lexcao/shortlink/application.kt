package io.github.lexcao.shortlink

import io.github.lexcao.shortlink.common.Link
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.Position
import kotlinx.css.TextAlign
import kotlinx.css.TextTransform
import kotlinx.css.Visibility
import kotlinx.css.backgroundColor
import kotlinx.css.border
import kotlinx.css.borderBottom
import kotlinx.css.borderRadius
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.height
import kotlinx.css.margin
import kotlinx.css.marginLeft
import kotlinx.css.padding
import kotlinx.css.paddingRight
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.properties.boxShadow
import kotlinx.css.px
import kotlinx.css.right
import kotlinx.css.textAlign
import kotlinx.css.textTransform
import kotlinx.css.top
import kotlinx.css.visibility
import kotlinx.css.width
import kotlinx.css.zIndex
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.child
import react.dom.form
import react.dom.render
import react.functionalComponent
import react.useState
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledH1
import styled.styledH3
import styled.styledImg
import styled.styledInput
import styled.styledLabel
import styled.styledSpan
import kotlin.browser.document
import kotlin.browser.window

val endpoint = window.location.origin
val scope = MainScope()
val client = HttpClient {
    install(JsonFeature) { serializer = KotlinxSerializer() }
}

suspend fun createLink(link: Link): Link {
    return client.post(endpoint + Link.path) {
        contentType(ContentType.Application.Json)
        body = link
    }
}

fun main() {
    render(document.getElementById("root")) {
        child(App)
    }
}

val App = functionalComponent<RProps> { _ ->
    val (link, setLink) = useState(Link(name = "", url = ""))
    val (message, setMessage) = useState("")

    val handleSubmit: (Event) -> Unit = it@{
        it.preventDefault()

        if (!Link.urlRegex.matches(link.url)) {
            setMessage("Link is invalid, should begin with http(s).")
            return@it
        }

        if (link.name.isNotBlank() && !Link.nameRegex.matches(link.name)) {
            setMessage("Name is invalid")
            return@it
        }

        scope.launch {
            setMessage("creating...")
            val result = createLink(link)
            setMessage(endpoint + "/" + result.name)
        }
        setLink(Link("", ""))
        setMessage("")
    }

    val handleNameChange: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setLink(Link(value, link.url))
    }

    val handleURLChange: (Event) -> Unit = {
        val value = (it.target as HTMLInputElement).value
        setLink(Link(link.name, value))
    }

    styledDiv {

        css {
            width = 400.px
            margin(48.px, LinearDimension.auto)
            padding(48.px)
            boxShadow(
                color = Color.gray,
                blurRadius = 2.px
            )
            borderRadius = 4.px
            fontFamily = "Helvetica,Arial,sans-serif"
        }

        styledH1 {
            css {
                textAlign = TextAlign.center
            }
            +"Short Link"
        }
        form {
            attrs.onSubmitFunction = handleSubmit
            textInput(
                name = "Name",
                value = link.name,
                onChange = handleNameChange
            )
            textInput(
                name = "URL",
                value = link.url,
                onChange = handleURLChange
            )

            message(text = message)

            styledButton(type = ButtonType.submit) {
                css {
                    display = Display.block
                    width = 100.pct
                    backgroundColor = Color.lightBlue
                    color = Color.white
                    fontSize = 24.px
                    border = "none"
                    textTransform = TextTransform.uppercase
                }

                +"create"
            }
        }
    }
}

private fun RBuilder.textInput(name: String, value: String, onChange: (Event) -> Unit) {
    styledLabel {
        css {
            display = Display.flex
            padding(16.px, 0.px)
        }

        styledSpan {
            css {
                paddingRight = 8.px
                width = 48.px
            }
            +name
        }
        styledInput(InputType.text) {
            css {
                width = 100.pct
                padding(5.px)
            }

            attrs.value = value
            attrs.onChangeFunction = onChange
        }
    }
}

private fun RBuilder.message(text: String) {
    val show = text.isNotEmpty()
    val success = text.startsWith("http")
    val initTips = { if (success) "Click to Copy" else "" }

    println(text)
    println(success)
    val (tips, setTips) = useState(initTips)

    styledDiv {
        css {
            visibility = if (show) Visibility.visible else Visibility.hidden
            position = Position.relative
            display = Display.block
            textAlign = TextAlign.center
        }
        styledH3 {
            css {
                display = Display.inlineBlock
                cursor = Cursor.pointer
                margin(8.px)
                borderBottom = "1px dotted black"
            }
            attrs.onMouseOverFunction = {
                setTips(initTips())
            }
            attrs.onMouseOutFunction = {
                setTips("")
            }
            attrs.onClickFunction = it@{
                if (!success) return@it
                window.navigator.clipboard.writeText(text)
                    .then {
                        setTips("Copied")
                        window.setTimeout(
                            handler = { setTips(initTips()) },
                            timeout = 1000
                        )
                    }
            }
            +text
        }
        styledSpan {
            css {
                visibility = if (tips.isEmpty()) Visibility.hidden else Visibility.visible
                position = Position.absolute
                backgroundColor = Color.black
                width = 120.px
                color = Color.white
                padding(5.px, 0.px)
                borderRadius = 6.px
                zIndex = 1
                marginLeft = 8.px
                height = 18.px
                top = 4.px
            }
            +tips
        }
        styledImg(src = Icon.qrCode) {
            css {
                position = Position.absolute
                visibility = Visibility.hidden
                right = 0.px
                height = 24.px
                width = 24.px
                top = 50.pct - 12.px
                cursor = Cursor.pointer
            }
            // TODO Show the qrCode when hovering the image
        }
    }
}
