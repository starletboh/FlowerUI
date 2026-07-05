package me.starletboh.flowerui.events.input


sealed class MouseEvent : InputEvent() {

    data class Move(
        val x: Double,
        val y: Double
    ) : MouseEvent()

    data class Click(
        val x: Double,
        val y: Double,
        val button: Int
    ) : MouseEvent()

    data class Release(
        val x: Double,
        val y: Double,
        val button: Int
    ) : MouseEvent()

    data class Scroll(
        val x: Double,
        val y: Double,
        val horizontal: Double,
        val vertical: Double,
//        val delta: Double
    ) : MouseEvent()
}