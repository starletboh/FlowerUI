package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext


class CheckboxWidget : Widget() {

    var checked: Boolean = false
    var radius: Int = 4
    var onCheckedChanged: ((Boolean) -> Unit)? = null

    var boxColor: Int? = null
    var checkedColor: Int? = null
    var borderColor: Int? = null

    private var hovered = false
    private var textureCacheKey: String? = null
    private var texture: Any? = null

    override fun render(ctx: RenderContext) {

        val fill = when {
            checked -> checkedColor ?: ctx.theme.colors.primary
            hovered -> ctx.theme.colors.hover
            else -> boxColor ?: ctx.theme.colors.surface
        }

        val key = "$width:$height:$fill:$radius:$checked:$hovered"
        if (key != textureCacheKey) {
            val svg = SvgBuilder.roundedRect(
                width.toInt().coerceAtLeast(1),
                height.toInt().coerceAtLeast(1),
                radius,
                fill.toHex()
            )
            texture = SvgTextureManager.getSvgTexture(
                id = key,
                svg = svg,
                width = width.toInt().coerceAtLeast(1),
                height = height.toInt().coerceAtLeast(1)
            )
            textureCacheKey = key
        }

        texture?.let {
            ctx.scope.drawTexture(it, globalX(), globalY(), width, height, 1f)
        }

        if (checked) {



            val thickness = (width * 0.14f).coerceAtLeast(1f)
            ctx.scope.drawOutline(
                globalX() + width * 0.2f,
                globalY() + height * 0.2f,
                width * 0.6f,
                height * 0.6f,
                ctx.theme.colors.background,
                thickness
            )
        } else {
            ctx.scope.drawOutline(
                globalX(), globalY(), width, height,
                borderColor ?: ctx.theme.colors.border
            )
        }

        renderChildren(ctx)
    }

    override fun onEvent(event: InputEvent): Boolean {
        when (event) {

            is MouseEvent.Move -> {
                hovered = contains(event.x, event.y)
            }

            is MouseEvent.Click -> {
                return contains(event.x, event.y)
            }

            is MouseEvent.Release -> {
                if (!contains(event.x, event.y)) return false
                checked = !checked
                onCheckedChanged?.invoke(checked)
                return true
            }
        }
        return false
    }
}
