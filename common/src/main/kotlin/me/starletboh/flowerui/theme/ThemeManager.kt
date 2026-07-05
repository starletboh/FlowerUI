package me.starletboh.flowerui.theme

import me.starletboh.flowerui.config.ThemeConfig

object ThemeManager {
    private var currentTheme: ThemeDefinition =
        ThemeRegistry.getOrFallback("catppuccin_mocha")
    private val listeners = mutableSetOf<(ThemeDefinition) -> Unit>()

    var config: ThemeConfig = ThemeConfig()

    fun current(): ThemeDefinition = currentTheme

    fun setTheme(id: String) {
        config.currentThemeId = id

        currentTheme = ThemeRegistry.getOrFallback(id)

        notifyListeners(currentTheme)
    }

    fun addListener(listener: (ThemeDefinition) -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: (ThemeDefinition) -> Unit) {
        listeners -= listener
    }
    fun resolveTheme(id: String): ThemeDefinition {
        return ThemeRegistry.get(id)
            ?: ThemeRegistry.getOrFallback("flower_dark")
    }
    private fun notifyListeners(theme: ThemeDefinition) {
        listeners.forEach { it(theme) }
    }
}