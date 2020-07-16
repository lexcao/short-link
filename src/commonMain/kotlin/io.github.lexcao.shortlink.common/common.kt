package io.github.lexcao.shortlink.common

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val name: String? = null,
    val url: String
) {
    companion object {
        const val path: String = "/link"
    }
}