package me.starletboh.flowerui.config

import me.starletboh.flowerui.animation.EasingType

data class AnimationConfig(

    // Global toggle

    var animationsEnabled: Boolean = true,

    var reduceMotion: Boolean = false,


    // Global speed control

    var speedMultiplier: Float = 1.0f,

    var hoverSpeedMultiplier: Float = 1.0f,

    var transitionSpeedMultiplier: Float = 1.0f,


    // Timing defaults

    var defaultDurationMs: Long = 180L,

    var fastDurationMs: Long = 90L,

    var slowDurationMs: Long = 300L,


    // Easing behavior (global defaults)

    var defaultEasing: EasingType = EasingType.EASE_OUT,



    var transitionEasing: EasingType = EasingType.EASE_IN_OUT,


    // UI interaction animations

    var enableHoverAnimations: Boolean = true,

    var enableClickAnimations: Boolean = true,

    var enableFocusAnimations: Boolean = true,


    // Layout animations

    var animateLayoutChanges: Boolean = true,

    var layoutAnimationDurationMs: Long = 200L,


    // Visual effects

    var enableFadeTransitions: Boolean = true,

    var enableScaleTransitions: Boolean = true,

    var enableSlideTransitions: Boolean = true,


    // Performance / accessibility

    var maxConcurrentAnimations: Int = 32,

    var preferPerformanceOverSmoothness: Boolean = false
)