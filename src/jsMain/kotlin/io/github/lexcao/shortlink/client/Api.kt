package io.github.lexcao.shortlink.client

import io.github.lexcao.shortlink.common.Link
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlin.browser.window

object Api {
    private val endpoint = window.location.origin
    private val client = HttpClient {
        install(JsonFeature) { serializer = KotlinxSerializer() }
    }

    suspend fun createLink(link: Link): Link {
        return client.post(endpoint + Link.path) {
            contentType(ContentType.Application.Json)
            body = link
        }
    }

    fun getURL(name: String): String = "$endpoint/$name"
}