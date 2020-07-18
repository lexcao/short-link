package io.github.lexcao.shortlink.server

import io.github.lexcao.shortlink.common.Link
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.features.BadRequestException
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext

typealias Handler = PipelineContext<Unit, ApplicationCall>

suspend fun Handler.indexPage() {
    call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
    )
}

suspend fun Handler.createLink() {
    var link = call.receive<Link>()

    if (!Link.urlRegex.matches(link.url)) {
        throw BadRequestException("URL is required")
    }

    if (link.name.trim().isBlank()) {
        link = link.copy(name = System.currentTimeMillis().base62())
    } else if (!Link.nameRegex.matches(link.name)) {
        throw BadRequestException("Name is invalid")
    }

    Store.save(link)
    call.respond(link)
}

suspend fun Handler.getByName() {
    val name: String = call.parameters["name"]!!
    Store.findByName(name)?.run {
        call.respondRedirect(url)
    }
}
