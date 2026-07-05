package me.starletboh.flowerui.input

import me.starletboh.flowerui.events.input.*
import me.starletboh.flowerui.ui.components.Component

object InputRouter {

    private val roots = mutableListOf<Component>()

    private var focused: Component? = null
    private var hovered: Component? = null

    /** The component that currently has input focus (last clicked hit target), or null. */
    val currentFocus: Component?
        get() = focused

    // ------------------------------------------------------------
    // REGISTRATION
    // ------------------------------------------------------------

    fun register(component: Component) {
        if (!roots.contains(component)) {
            roots += component
        }
    }

    fun unregister(component: Component) {
        roots -= component

        if (focused == component) focused = null
        if (hovered == component) hovered = null
    }

    // ------------------------------------------------------------
    // MAIN ENTRY
    // ------------------------------------------------------------

    fun handle(event: InputEvent) {

        if (event.consumed) return

        when (event) {

            is MouseEvent.Click -> handleClick(event)
            is MouseEvent.Release -> handleRelease(event)
            is MouseEvent.Move -> handleMove(event)
            is MouseEvent.Scroll -> handleScroll(event)

            is KeyEvent.Press,
            is KeyEvent.Release,
            is KeyEvent.CharTyped -> handleKey(event)
        }
    }

    // ------------------------------------------------------------
    // MOUSE CLICK
    // ------------------------------------------------------------

    private fun handleClick(event: MouseEvent.Click) {

        val target = hitTest(event.x, event.y)

        // Clicking empty space (nothing hit) should still drop focus from
        // whatever was previously focused (e.g. a text input), otherwise
        // it stays visually focused/blinking forever.
        focused = target

        if (target == null) return

        // bubble DOWN to target
        if (target.dispatchEvent(event)) {
            event.consume()
        }
    }

    // ------------------------------------------------------------
    // MOUSE RELEASE
    // ------------------------------------------------------------

    private fun handleRelease(event: MouseEvent.Release) {

        focused?.let { focus ->
            if (focus.dispatchEvent(event)) {
                event.consume()
            }
        }
    }

    // ------------------------------------------------------------
    // MOUSE MOVE (HOVER SYSTEM)
    // ------------------------------------------------------------

    private fun handleMove(event: MouseEvent.Move) {

        val newHovered = hitTest(event.x, event.y)

        if (newHovered != hovered) {

            // optional: exit event
            hovered?.onEvent(event)

            hovered = newHovered
        }

        hovered?.onEvent(event)
    }

    // ------------------------------------------------------------
    // SCROLL
    // ------------------------------------------------------------

    private fun handleScroll(event: MouseEvent.Scroll) {

        hitTest(event.x, event.y)
            ?.let {
                if (it.dispatchEvent(event)) {
                    event.consume()
                }
            }
    }

    // ------------------------------------------------------------
    // KEYBOARD
    // ------------------------------------------------------------

    private fun handleKey(event: InputEvent) {

        focused?.let {
            if (it.dispatchEvent(event)) {
                event.consume()
            }
        }
    }

    // ------------------------------------------------------------
    // HIT TEST (FIXED - IMPORTANT PART)
    // ------------------------------------------------------------

    private fun hitTest(x: Double, y: Double): Component? {

        for (root in roots.asReversed()) {
            val hit = root.hitTestDeep(x, y)
            if (hit != null) return hit
        }

        return null
    }
}