package me.starletboh.flowerui.events.input

abstract class InputEvent {
    var consumed: Boolean = false
        private set

    fun consume() {
        consumed = true
    }
}