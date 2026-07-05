package me.starletboh.flowerui.theme

import me.starletboh.flowerui.theme.presets.CatppuccinMocha
import me.starletboh.flowerui.theme.presets.flowerDark
import me.starletboh.flowerui.theme.presets.flowerLight

object BuiltInThemes {

    fun registerAll() {
        ThemeRegistry.register(CatppuccinMocha())
        ThemeRegistry.register(flowerDark())
        ThemeRegistry.register(flowerLight())
    }
}