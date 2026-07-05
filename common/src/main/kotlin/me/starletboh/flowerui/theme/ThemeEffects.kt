package me.starletboh.flowerui.theme

data class ThemeEffects(

    // Shadow strength for elevated surfaces
    var shadowStrength: Float = 0.2f,

    // Blur behind panels (glass-like UI)
    var blurStrength: Float = 0f,

    // Global corner rounding multiplier
    var cornerRadiusMultiplier: Float = 1.0f,

    // Whether UI uses "flat" or "elevated" style
    var elevationEnabled: Boolean = true,

    // Opacity softness for surfaces
    var surfaceAlpha: Float = 1.0f
)