package me.starletboh.flowerui.theme

data class ThemeContext(
    val base: ThemeDefinition,
    val override: ThemeDefinition? = null
) {
    companion object {
        fun default(): ThemeContext {
            return ThemeContext(
                ThemeManager.resolveTheme("flower_dark")
            )
        }
    }
    fun resolve(): ThemeDefinition = override ?: base
}