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
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.TextAlign
import kotlinx.css.TextTransform
import kotlinx.css.backgroundColor
import kotlinx.css.border
import kotlinx.css.borderRadius
import kotlinx.css.color
import kotlinx.css.display
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.margin
import kotlinx.css.padding
import kotlinx.css.paddingRight
import kotlinx.css.pct
import kotlinx.css.properties.boxShadow
import kotlinx.css.px
import kotlinx.css.textAlign
import kotlinx.css.textTransform
import kotlinx.css.width
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
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

suspend fun createLink(link: Link) {
    client.post<Unit>(endpoint + Link.path) {
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
    val (link, setLink) = useState { Link(name = "", url = "") }

    val handleSubmit: (Event) -> Unit = {
        it.preventDefault()

        scope.launch {
            createLink(link)
        }
        setLink(Link("", ""))
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
            width = 80.pct
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
