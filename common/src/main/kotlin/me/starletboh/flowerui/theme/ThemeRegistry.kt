package me.starletboh.flowerui.theme


object ThemeRegistry {

    private val themes = mutableMapOf<String, ThemeDefinition>()

    fun register(theme: ThemeDefinition) {
        themes[theme.id] = theme
    }

    fun get(id: String): ThemeDefinition? {
        return themes[id]
    }

    fun getOrFallback(id: String): ThemeDefinition {
        return themes[id]
            ?: themes["flower_dark"]
            ?: error("No fallback theme registered")
    }

    fun all(): Collection<ThemeDefinition> = themes.values
}