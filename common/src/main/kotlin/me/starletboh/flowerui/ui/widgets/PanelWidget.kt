package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext

class PanelWidget : Widget() {

    var backgroundColor: Int? = null
    var radius: Int = 8

    init {




        clipChildren = true
    }

    private var texture: Any? = null
    private var cacheKey: String? = null

    override fun render(ctx: RenderContext) {

        val base = ctx.theme.colors.surface

        val fill = backgroundColor ?: base

        val svg = SvgBuilder.roundedRect(
            width.toInt().coerceAtLeast(1),
            height.toInt().coerceAtLeast(1),
            radius,
            fill.toHex()
        )

        val key = "$width:$height:$fill:$radius"

        if (key != cacheKey) {
            texture = SvgTextureManager.getSvgTexture(
                id = key,
                svg = svg,
                width = width.toInt(),
                height = height.toInt()
            )
            cacheKey = key
        }

        texture?.let {
            ctx.scope.drawTexture(it, globalX(), globalY(), width, height, 1f)
        }

        renderChildren(ctx)
    }
}