package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.ui.layout.Align
import me.starletboh.flowerui.ui.render.RenderContext

/**
 * Simple text label widget. Sizes itself to the measured text unless a
 * fixed [width]/[height] has already been assigned.
 */
class TextWidget(var text: String = "") : Widget() {

    var color: Int? = null
    var scale: Float = 1f
    var align: Align = Align.START

    override fun render(ctx: RenderContext) {

        val textWidth = ctx.scope.measureTextWidth(text, scale)
        val textHeight = ctx.scope.measureTextHeight(text, scale)

        // Auto-size to the measured text unless the caller has already
        // given this label an explicit width/height.
        if (width == 0f) width = textWidth
        if (height == 0f) height = textHeight

        val drawX = when (align) {
            Align.START -> globalX()
            Align.CENTER -> globalX() + (width - textWidth) / 2f
            Align.END -> globalX() + width - textWidth
        }

        ctx.scope.drawText(
            text,
            drawX,
            globalY(),
            color ?: ctx.theme.colors.textPrimary,
            scale
        )

        renderChildren(ctx)
    }
}
