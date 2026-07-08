package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.ui.render.RenderContext


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
