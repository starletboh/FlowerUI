package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.ui.components.Component

abstract class Widget : Component() {

    var id: String? = null
    fun Int.toHex(): String {
        val r = (this shr 16) and 0xFF
        val g = (this shr 8) and 0xFF
        val b = this and 0xFF
        return String.format("#%02X%02X%02X", r, g, b)
    }

}