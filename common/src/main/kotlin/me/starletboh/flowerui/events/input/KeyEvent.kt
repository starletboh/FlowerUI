package me.starletboh.flowerui.events.input

sealed class KeyEvent : InputEvent() {

    abstract val keyCode: Int
    abstract val scanCode: Int
    abstract val modifiers: Int

    data class Press(
        override val keyCode: Int,
        override val scanCode: Int,
        override val modifiers: Int,
//        override var consumed: Boolean = false
    ) : KeyEvent()

    data class Release(
        override val keyCode: Int,
        override val scanCode: Int,
        override val modifiers: Int,
//        override var consumed: Boolean = false
    ) : KeyEvent()

    data class CharTyped(
        val char: Char,
//        override var consumed: Boolean = false
    ) : KeyEvent() {
        override val keyCode: Int = 0
        override val scanCode: Int = 0
        override val modifiers: Int = 0
    }
}