package io.github.lexcao.shortlink.server

import io.github.lexcao.shortlink.common.Link
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post

fun Routing.routes() {
    static("") {
        resource("/short-link.js", "short-link.js")
    }
    get("/") { indexPage() }
    post(Link.path) { createLink() }
    get("/{name}") { getByName() }
}
