package me.starletboh.flowerui.ui.components

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.ui.render.RenderContext

/**
 * Implemented by widgets that need part of themselves (e.g. an open
 * dropdown's item list) to render *outside* their normal place in the
 * tree - on top of everything, and not clipped by whatever container
 * they're logically sitting inside.
 *
 * The normal [Component.render]/[Component.hitTestDeep] path is untouched;
 * this is an *additional* draw+hit-test pass that RootComponent runs after
 * (visually on top of) its normal children.
 */
interface Overlay {
    /** Draw the overlay content. Runs after all normal children, unclipped. */
    fun renderOverlay(ctx: RenderContext)

    /** Return a hit-test target (usually `this`) if (x, y) hits the overlay content, else null. */
    fun overlayHitTest(x: Double, y: Double): Component?
}

class RootComponent : Component() {

    // NOTE: these are widgets that are *also* normal children (added via
    // add()) - registering here doesn't move them out of the tree, it just
    // asks RootComponent to additionally draw/hit-test their overlay
    // content on top of everything, unclipped.
    private val overlays = mutableListOf<Overlay>()

    fun showOverlay(overlay: Overlay) {
        if (overlay !in overlays) overlays += overlay
    }

    fun hideOverlay(overlay: Overlay) {
        overlays -= overlay
    }

    fun handleInput(event: InputEvent) {
        dispatchEvent(event)
    }

    override fun render(ctx: RenderContext) {
        renderChildren(ctx)
        // most-recently-shown overlay last, so it visually wins if two ever overlap
        overlays.forEach { it.renderOverlay(ctx) }
    }

    override fun hitTestDeep(x: Double, y: Double): Component? {
        // overlays sit on top visually, so they get first refusal on hit-testing too
        for (overlay in overlays.asReversed()) {
            overlay.overlayHitTest(x, y)?.let { return it }
        }
        return super.hitTestDeep(x, y)
    }
}