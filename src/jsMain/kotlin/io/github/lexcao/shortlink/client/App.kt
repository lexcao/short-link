package io.github.lexcao.shortlink.client

import io.github.lexcao.shortlink.common.Link
import io.github.lexcao.shortlink.scope
import kotlinx.coroutines.launch
import kotlinx.html.ButtonType
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.events.Event
import react.RProps
import react.dom.button
import react.dom.form
import react.dom.h1
import react.functionalComponent
import react.useState
import styled.css
import styled.styledDiv

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
            val result = Api.createLink(link)
            setMessage(Api.getURL(result.name))
        }
        setLink(Link("", ""))
        setMessage("")
    }

    val handleNameChange: (String) -> Unit = {
        setLink(Link(it, link.url))
    }

    val handleURLChange: (String) -> Unit = {
        setLink(Link(link.name, it))
    }

    styledDiv {

        css { +CSS.card }

        h1 { +"Short Link" }
        form {
            attrs.onSubmitFunction = handleSubmit

            textInput {
                name = "Name"
                value = link.name
                onChange = handleNameChange
            }

            textInput {
                name = "URL"
                value = link.url
                onChange = handleURLChange
            }

            message {
                text = message
                permitCopy = message.startsWith("http")
            }
            button(type = ButtonType.submit) { +"create" }
        }
    }
}
