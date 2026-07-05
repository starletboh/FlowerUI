package me.starletboh.flowerui.config

data class ThemeConfig(

    var currentThemeId: String = "flower_dark",

    var fallbackThemeId: String = "flower_light",

    var enableSystemThemeSync: Boolean = false
)