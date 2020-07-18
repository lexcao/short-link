package io.github.lexcao.shortlink.client

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onMouseOutFunction
import kotlinx.html.js.onMouseOverFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RProps
import react.ReactElement
import react.child
import react.dom.h3
import react.dom.input
import react.dom.span
import react.functionalComponent
import react.useState
import styled.css
import styled.styledDiv
import styled.styledLabel
import kotlin.browser.window

external interface InputProps : RProps {
    var name: String
    var value: String
    var onChange: (String) -> Unit
}

val TextInput = functionalComponent<InputProps> { props ->
    styledLabel {
        css { +CSS.textInput }
        span { +props.name }
        input(InputType.text) {
            attrs.value = props.value
            attrs.onChangeFunction = {
                props.onChange((it.target as HTMLInputElement).value)
            }
        }
    }
}

fun RBuilder.textInput(handler: InputProps.() -> Unit): ReactElement = child(TextInput) {
    attrs { handler() }
}

//////////////////////////////////////////////////////////////////////////////////////////

external interface MessageProps : RProps {
    var text: String
    var permitCopy: Boolean
}

private val Message = functionalComponent<MessageProps> { props ->
    val text = props.text
    val permitCopy = props.permitCopy
    val initTips = { if (permitCopy) "Click to Copy" else "" }
    val (tips, setTips) = useState(initTips)

    val copyText: (Event) -> Unit = it@{
        if (!permitCopy) return@it
        window.navigator.clipboard.writeText(text)
            .then { setTips("Copied") }
    }

    styledDiv {
        css { +CSS.message }

        if (text.isNotEmpty()) {
            h3 {
                attrs {
                    onMouseOverFunction = { setTips(initTips()) }
                    onMouseOutFunction = { setTips("") }
                    onClickFunction = copyText
                }

                +text
            }
        }

        if (permitCopy && tips.isNotEmpty()) {
            span { +tips }
        }

        /*styledImg(src = Icon.qrCode) {
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
        }*/
    }
}

fun RBuilder.message(handler: MessageProps.() -> Unit): ReactElement = child(Message) {
    attrs { handler() }
}
