@file:JsModule("qrcode")
@file:JsNonModule

package io.github.lexcao.shortlink.client

import kotlin.js.Promise

/**
 *  @link https://github.com/soldair/node-qrcode#todataurltext-options-cberror-url-1
 */
@JsName("toDataURL")
external fun generateQRCodeImageString(text: String): Promise<String>
