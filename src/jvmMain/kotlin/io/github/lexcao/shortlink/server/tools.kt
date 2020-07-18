package io.github.lexcao.shortlink.server

private const val BASE: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

fun Long.base62(): String {
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
