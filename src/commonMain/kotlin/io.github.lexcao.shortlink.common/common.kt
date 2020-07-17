package io.github.lexcao.shortlink.common

import kotlinx.serialization.Serializable

@Serializable
data class Link(
    val name: String = "",
    val url: String
) {
    companion object {
        const val path: String = "/link"
        val urlRegex: Regex =
            "^https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)".toRegex()
        val nameRegex: Regex = "[\\w-_]*".toRegex()
    }
}