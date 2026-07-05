package me.starletboh.flowerui.events

object UIInvalidation {

    private val listeners = mutableSetOf<() -> Unit>()

    fun invalidateAll() {
        listeners.forEach { it() }
    }

    fun register(listener: () -> Unit) {
        listeners += listener
    }

    fun unregister(listener: () -> Unit) {
        listeners -= listener
    }
}