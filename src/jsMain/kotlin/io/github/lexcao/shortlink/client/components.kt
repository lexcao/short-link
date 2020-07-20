package io.github.lexcao.shortlink.client

import io.github.lexcao.shortlink.scope
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
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
import react.dom.img
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

        if (permitCopy) {
            qrCodeImage(text)
        }
    }
}

fun RBuilder.message(handler: MessageProps.() -> Unit): ReactElement = child(Message) {
    attrs { handler() }
}

//////////////////////////////////////////////////////////////////////////////////////////

external interface QRCodeImageProps : RProps {
    var text: String
}

private val QRCodeImage = functionalComponent<QRCodeImageProps> { props ->
    val text = props.text
    val (imageString, setImageString) = useState("")

    if (text.isNotEmpty()) {
        scope.launch {
            setImageString(generateQRCodeImageString(text).await())
        }
    }

    if (imageString.isNotEmpty()) {
        img(src = imageString) {}
    }
}

private fun RBuilder.qrCodeImage(text: String): ReactElement = child(QRCodeImage) {
    attrs { this.text = text }
}
