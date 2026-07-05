package me.starletboh.flowerui.theme.style

data class ComponentStyle(

    var background: Int = 0xFFFFFFFF.toInt(),
    var border: Int? = null,

    var hoverBackground: Int? = null,
    var pressedBackground: Int? = null,

    var radius: Int = 6
)