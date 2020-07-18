package io.github.lexcao.shortlink.client

import kotlinx.css.Color
import kotlinx.css.Cursor
import kotlinx.css.Display
import kotlinx.css.LinearDimension
import kotlinx.css.Position
import kotlinx.css.TextAlign
import kotlinx.css.TextTransform
import kotlinx.css.backgroundColor
import kotlinx.css.border
import kotlinx.css.borderBottom
import kotlinx.css.borderRadius
import kotlinx.css.button
import kotlinx.css.color
import kotlinx.css.cursor
import kotlinx.css.display
import kotlinx.css.fontFamily
import kotlinx.css.fontSize
import kotlinx.css.h1
import kotlinx.css.h3
import kotlinx.css.height
import kotlinx.css.input
import kotlinx.css.margin
import kotlinx.css.marginLeft
import kotlinx.css.padding
import kotlinx.css.paddingRight
import kotlinx.css.pct
import kotlinx.css.position
import kotlinx.css.properties.boxShadow
import kotlinx.css.px
import kotlinx.css.span
import kotlinx.css.textAlign
import kotlinx.css.textTransform
import kotlinx.css.top
import kotlinx.css.width
import kotlinx.css.zIndex
import styled.StyleSheet

object CSS : StyleSheet("ComponentStyles") {

    val card by css {
        width = 400.px
        margin(48.px, LinearDimension.auto)
        padding(48.px)
        boxShadow(
            color = Color.gray,
            blurRadius = 2.px
        )
        borderRadius = 4.px
        fontFamily = "Helvetica,Arial,sans-serif"

        h1 {
            textAlign = TextAlign.center
        }

        button {
            display = Display.block
            width = 100.pct
            backgroundColor = Color.lightBlue
            color = Color.white
            fontSize = 24.px
            border = "none"
            textTransform = TextTransform.uppercase
        }
    }

    val textInput by css {
        display = Display.flex
        padding(16.px, 0.px)

        span {
            paddingRight = 8.px
            width = 48.px
        }

        input {
            width = 100.pct
            padding(5.px)
        }
    }

    val message by css {
        position = Position.relative
        display = Display.block
        textAlign = TextAlign.center

        h3 {
            display = Display.inlineBlock
            cursor = Cursor.pointer
            margin(8.px)
            borderBottom = "1px dotted black"
        }

        span {
            position = Position.absolute
            backgroundColor = Color.black
            width = 120.px
            color = Color.white
            padding(5.px, 0.px)
            borderRadius = 6.px
            zIndex = 1
            marginLeft = 8.px
            height = 18.px
            top = 4.px
        }
    }
}