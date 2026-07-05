package me.starletboh.flowerui.config

data class InputConfig(

    // Global input behavior

    var enableMouseInput: Boolean = true,

    var enableKeyboardInput: Boolean = true,

    var enableControllerInput: Boolean = false,


    // Hover behavior

    var hoverRequiresFocus: Boolean = false,

    var hoverDelayMs: Long = 0L,

    var hoverSensitivity: Float = 1.0f,


    // Click behavior

    var clickTolerance: Float = 2f,

    var doubleClickWindowMs: Long = 250L,

    var rightClickEnabled: Boolean = true,

    var middleClickEnabled: Boolean = false,


    // Drag behavior

    var enableDrag: Boolean = true,

    var dragThreshold: Float = 3f,

    var dragSensitivity: Float = 1.0f,


    // Keyboard navigation

    var enableTabNavigation: Boolean = true,

    var tabWrapAround: Boolean = true,

    var arrowKeyNavigation: Boolean = true,


    // Focus system

    var focusOnClick: Boolean = true,

    var focusOnHover: Boolean = false,

    var clearFocusOnClickOutside: Boolean = true,


    // Text input behavior

    var enableTextInput: Boolean = true,

    var cursorBlinkRateMs: Long = 500L,

    var selectAllOnFocus: Boolean = false,


    // Scrolling

    var scrollSpeed: Float = 1.0f,

    var smoothScrolling: Boolean = true,

    var scrollAcceleration: Float = 1.0f,


    // Debug input

    var logInputEvents: Boolean = false,

    var showInputHitboxes: Boolean = false
)