package io.github.lexcao.shortlink

import io.github.lexcao.shortlink.common.Link
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.html.ButtonType
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.main
import kotlinx.html.postForm
import kotlinx.html.textInput
import kotlinx.html.title
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private val store = ConcurrentHashMap<String, Link>()

fun Application.main() {

    install(DefaultHeaders)
    install(ContentNegotiation) { jackson() }

    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        post("/link") {
            val link = call.receive<Link>()

            // TODO validation

            val name = link.name ?: UUID.randomUUID().toString()

            val copy = link.copy(name = name)
            store[name] = copy

            call.respond(copy)
        }
        get("/{name}") {
            val name: String = call.parameters["name"]!!
            store[name]?.run {
                call.respondRedirect(url)
            }
            call.respondHtml(HttpStatusCode.NotFound, HTML::notFound)
        }
    }
}

fun HTML.index() {
    head {
        title("S-L Short Link")
    }
    body {
        header {
            h1 { +"S-L Short Link" }
        }
        main {
            postForm {
                id = "form"

                label { +"Name" }
                textInput(name = "name")
                label { +"URL" }
                textInput(name = "url")
                button(type = ButtonType.submit) { +"Create" }
            }
        }
    }
}

fun HTML.notFound() {
    head {
        title("404 not found")
    }
    body {
        header {
            h1 { +"404 Sorry, not Found." }
        }
    }
}