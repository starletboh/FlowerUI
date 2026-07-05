package me.starletboh.flowerui.ui.layout

data class LayoutBox(
    val startX: Float = 0f,
    val startY: Float = 0f,
    val alignX: Align = Align.START,
    val alignY: Align = Align.START,
    val gap: Float = 4f,
    val padding: Float = 0f
)

enum class Align {
    START,
    CENTER,
    END
}