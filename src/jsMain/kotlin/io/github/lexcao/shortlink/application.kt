package io.github.lexcao.shortlink

import io.github.lexcao.shortlink.client.App
import kotlinx.coroutines.MainScope
import react.child
import react.dom.render
import kotlin.browser.document

internal val scope = MainScope()

fun main() {
    render(document.getElementById("root")) { child(App) }
}
