package me.starletboh.flowerui.font

import me.starletboh.flowerui.theme.ThemeTypography

object FontManager {

    private var defaultFont: FontHandle? = null

    fun registerDefault(font: FontHandle) {
        defaultFont = font
    }

    fun resolve(typography: ThemeTypography): FontHandle {
        return defaultFont
            ?: error("No default font registered")
    }
}