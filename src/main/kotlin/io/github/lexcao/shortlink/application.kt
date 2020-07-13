package io.github.lexcao.shortlink

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.title
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private val store = ConcurrentHashMap<String, Link>()

fun Application.main() {

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
        }
    }
}

data class Link(
    val name: String? = null,
    val url: String
)

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}
