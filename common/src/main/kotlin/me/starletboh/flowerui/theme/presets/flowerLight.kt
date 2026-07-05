package me.starletboh.flowerui.theme.presets

import me.starletboh.flowerui.theme.ThemeColors
import me.starletboh.flowerui.theme.ThemeDefinition
import me.starletboh.flowerui.theme.ThemeEffects
import me.starletboh.flowerui.theme.ThemeTypography

fun flowerLight(): ThemeDefinition {

    return ThemeDefinition(
        id = "flower_light",
        name = "Flower Light",

        colors = ThemeColors(
            background = 0xFFF5F6FA.toInt(),
            surface = 0xFFFFFFFF.toInt(),

            primary = 0xFF3B6FFF.toInt(),
            secondary = 0xFF7C4DFF.toInt(),

            textPrimary = 0xFF1A1A1A.toInt(),
            textSecondary = 0xFF5F6368.toInt(),

            border = 0xFFE0E3E7.toInt(),

            hover = 0xFFF0F2F5.toInt(),
            pressed = 0xFFE6E9EF.toInt(),
            disabled = 0xFFE0E3E7.toInt()
        ),

        typography = ThemeTypography(
            fontFamily = "default",
            fontSize = 14f,
            fontWeight = 1.0f
        ),

        effects = ThemeEffects(
            shadowStrength = 0.15f,
            blurStrength = 0.0f,
            cornerRadiusMultiplier = 1.0f,
            elevationEnabled = true,
            surfaceAlpha = 1.0f
        )
    )
}