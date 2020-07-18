package io.github.lexcao.shortlink.server

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.style
import kotlinx.html.title

fun HTML.notFound() {
    head {
        title("URL Not Found")
    }
    body {
        style = "text-align: center;"
        h1 {
            style = "margin-top: 20vh;"
            +"🤔 URL not Found."
        }
        h2 {
            a(href = "/") {
                +"👉 Back to home."
            }
        }
    }
}
