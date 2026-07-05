package me.starletboh.flowerui.theme.presets

import me.starletboh.flowerui.theme.ThemeColors
import me.starletboh.flowerui.theme.ThemeDefinition
import me.starletboh.flowerui.theme.ThemeEffects
import me.starletboh.flowerui.theme.ThemeTypography


fun CatppuccinMocha(): ThemeDefinition {

    return ThemeDefinition(
        id = "catppuccin_mocha",
        name = "Catppuccin Mocha",

        colors = ThemeColors(
            background = 0xFF1E1E2E.toInt(),
            surface = 0xFF313244.toInt(),

            primary = 0xFF89B4FA.toInt(),
            secondary = 0xFFF5C2E7.toInt(),

            textPrimary = 0xFFCDD6F4.toInt(),
            textSecondary = 0xFFA6ADC8.toInt(),

            border = 0xFF45475A.toInt(),

            hover = 0xFF585B70.toInt(),
            pressed = 0xFF6C7086.toInt(),
            disabled = 0xFF313244.toInt()
        ),

        typography = ThemeTypography(
            fontFamily = "default",
            fontSize = 14f,
            fontWeight = 1.0f
        ),

        effects = ThemeEffects(
            shadowStrength = 0.3f,
            blurStrength = 0.15f,
            cornerRadiusMultiplier = 1.2f,
            elevationEnabled = true,
            surfaceAlpha = 0.95f
        )
    )
}