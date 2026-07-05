package me.starletboh.flowerui.theme.presets

import me.starletboh.flowerui.theme.ThemeColors
import me.starletboh.flowerui.theme.ThemeDefinition
import me.starletboh.flowerui.theme.ThemeEffects
import me.starletboh.flowerui.theme.ThemeTypography

fun flowerDark(): ThemeDefinition {

    return ThemeDefinition(
        id = "flower_dark",
        name = "Flower Dark",

        colors = ThemeColors(
            background = 0xFF0F1115.toInt(),
            surface = 0xFF171A21.toInt(),

            primary = 0xFF6C8CFF.toInt(),
            secondary = 0xFFB38CFF.toInt(),

            textPrimary = 0xFFE6E6E6.toInt(),
            textSecondary = 0xFF9AA0A6.toInt(),

            border = 0xFF2A2F3A.toInt(),

            hover = 0xFF232838.toInt(),
            pressed = 0xFF1C2130.toInt(),
            disabled = 0xFF2A2F3A.toInt()
        ),

        typography = ThemeTypography(
            fontFamily = "default",
            fontSize = 14f,
            fontWeight = 1.0f
        ),

        effects = ThemeEffects(
            shadowStrength = 0.35f,
            blurStrength = 0.0f,
            cornerRadiusMultiplier = 1.0f,
            elevationEnabled = true,
            surfaceAlpha = 1.0f
        )
    )
}