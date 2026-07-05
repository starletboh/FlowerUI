package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.ui.render.RenderContext

/**
 * Draws a texture directly (no SVG generation/caching - just a straight
 * `drawTexture` call), making this the cheapest widget in the set. Use it
 * for icons, logos, screenshots, avatars, etc.
 *
 * [texture] is whatever opaque handle your platform's `RenderScope.drawTexture`
 * expects (e.g. a Minecraft `Identifier` on Fabric).
 */
class ImageWidget() : Widget() {
    var texture: Any? = null
    var alpha: Float = 1f

    override fun render(ctx: RenderContext) {
        texture?.let {
            ctx.scope.drawTexture(it, globalX(), globalY(), width, height, alpha)
        }
        renderChildren(ctx)
    }
}
