package me.starletboh.flowerui.theme.adapters

import me.starletboh.flowerui.theme.ThemeDefinition
import me.starletboh.flowerui.theme.style.ButtonStyle


fun ThemeDefinition.buttonStyle(): ButtonStyle {
    return ButtonStyle(
        background = colors.surface,
        hoverBackground = colors.hover,
        pressedBackground = colors.pressed,
        textColor = colors.textPrimary,
        borderColor = colors.border,
        radius = (6 * effects.cornerRadiusMultiplier).toInt()
    )
}