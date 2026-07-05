package me.starletboh.flowerui.config.migration.patches

import me.starletboh.flowerui.config.FlowerConfig
import me.starletboh.flowerui.config.migration.ConfigPatch

class ThemeNormalizationPatch : ConfigPatch {

    override val id = "theme_normalization_v1"

    override fun apply(config: FlowerConfig): FlowerConfig {

        val theme = config.theme

        val fixed = theme.copy(
            currentThemeId = normalize(theme.currentThemeId),
            fallbackThemeId = normalize(theme.fallbackThemeId)
        )

        return config.copy(theme = fixed)
    }

    private fun normalize(id: String): String {
        return when (id.lowercase()) {
            "default" -> "flower_dark"
            "dark" -> "flower_dark"
            "light" -> "flower_light"
            else -> id
        }
    }
}