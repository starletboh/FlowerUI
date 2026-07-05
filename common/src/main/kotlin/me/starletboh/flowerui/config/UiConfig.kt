package me.starletboh.flowerui.config

data class UiConfig(

    // Global scaling

    var scale: Float = 1.0f,

    var density: Float = 1.0f,

    var adaptToGuiScale: Boolean = true,


    // Default sizing behavior

    var defaultElementHeight: Float = 24f,

    var defaultMinWidth: Float = 48f,

    var defaultCornerRadius: Float = 6f,

    var baseSpacing: Float = 4f,

    var defaultPadding: Float = 6f,


    // Interaction behavior

    var clickPadding: Float = 2f,

    var hoverScale: Float = 1.05f,

    var enableHoverAnimations: Boolean = true,

    var hoverAnimationSpeed: Float = 1.0f,


    // Animation system

    var animationSpeed: Float = 1.0f,

    var animationsEnabled: Boolean = true,

    var reduceMotion: Boolean = false,


    // Typography

    var fontScale: Float = 1.0f,

    var lineHeight: Float = 1.2f,

    var antiAliasedText: Boolean = true,


    // Rendering quality

    var highQualityRendering: Boolean = true,

    var enableBlur: Boolean = false,


    // Debug options

    var showBounds: Boolean = false,

    var showHitboxes: Boolean = false,

    var logEvents: Boolean = false,

    var showPerformanceOverlay: Boolean = false
)