package me.starletboh.flowerui.ui.components

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.ui.render.RenderContext


interface Overlay {
    
    fun renderOverlay(ctx: RenderContext)

    
    fun overlayHitTest(x: Double, y: Double): Component?
}

class RootComponent : Component() {





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

        overlays.forEach { it.renderOverlay(ctx) }
    }

    override fun hitTestDeep(x: Double, y: Double): Component? {

        for (overlay in overlays.asReversed()) {
            overlay.overlayHitTest(x, y)?.let { return it }
        }
        return super.hitTestDeep(x, y)
    }
}