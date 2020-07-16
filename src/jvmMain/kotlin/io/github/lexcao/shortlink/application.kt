package io.github.lexcao.shortlink

import io.github.lexcao.shortlink.common.Link
import io.github.lexcao.shortlink.server.Store
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.gzip
import io.ktor.http.ContentType
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlin.run

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port, module = Application::run).start(wait = true)
}

fun Application.run() {

    install(DefaultHeaders)
    install(ContentNegotiation) { json() }
    install(Compression) { gzip() }

    routing {
        static("") {
            resource("/short-link.js", "short-link.js")
        }
        get("/") {
            call.respondText(
                this::class.java.classLoader.getResource("index.html")!!.readText(),
                ContentType.Text.Html
            )
        }
        post(Link.path) {
            var link = call.receive<Link>()

            // TODO validation

            if (link.name.trim().isBlank()) {
                link = link.copy(name = System.currentTimeMillis().base62())
            }
            Store.save(link)

            call.respond(link)
        }
        get("/{name}") {
            val name: String = call.parameters["name"]!!
            Store.findByName(name)?.run {
                call.respondRedirect(url)
            }
        }
    }
}

private const val BASE: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

private fun Long.base62(): String {
    var num = this
    return buildString {
        do {
            val index = (num % BASE.length).toInt()
            append(BASE[index])
            num /= BASE.length
        } while (num > 0)
        reverse()
    }
}