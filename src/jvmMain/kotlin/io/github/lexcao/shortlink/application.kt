package io.github.lexcao.shortlink

import io.github.lexcao.shortlink.server.routes
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.gzip
import io.ktor.routing.Routing
import io.ktor.serialization.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090
    embeddedServer(Netty, port, module = Application::module).start(wait = true)
}

fun Application.module() {
    install(DefaultHeaders)
    install(ContentNegotiation) { json() }
    install(Compression) { gzip() }
    install(Routing) { routes() }
}
