package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext


class ToggleWidget : Widget() {

    var value: Boolean = false
        set(v) {
            if (field == v) return
            field = v
            onChange?.invoke(field)
        }

    var onChange: ((Boolean) -> Unit)? = null

    var knobRadius: Float = 6f
    var trackOnColor: Int? = null
    var trackOffColor: Int? = null
    var knobColor: Int? = null

    private var trackCacheKey: String? = null
    private var trackTexture: Any? = null
    private var knobCacheKey: String? = null
    private var knobTexture: Any? = null

    override fun render(ctx: RenderContext) {

        val bg = if (value) (trackOnColor ?: ctx.theme.colors.primary) else (trackOffColor ?: ctx.theme.colors.border)
        val w = width.toInt().coerceAtLeast(1)
        val h = height.toInt().coerceAtLeast(1)
        val radius = (h / 2).coerceAtLeast(1)

        val trackKey = "$w:$h:$bg:$radius"
        if (trackKey != trackCacheKey) {
            trackTexture = SvgTextureManager.getSvgTexture(
                "toggle_track:$trackKey",
                SvgBuilder.roundedRect(w, h, radius, bg.toHex()),
                w, h
            )
            trackCacheKey = trackKey
        }
        trackTexture?.let { ctx.scope.drawTexture(it, globalX(), globalY(), width, height, 1f) }

        val knobSize = knobRadius * 2f
        val knobPx = knobSize.toInt().coerceAtLeast(1)
        val knob = knobColor ?: 0xFFFFFFFF.toInt()
        val knobKey = "$knobPx:$knob"
        if (knobKey != knobCacheKey) {
            knobTexture = SvgTextureManager.getSvgTexture(
                "toggle_knob:$knobKey",
                SvgBuilder.roundedRect(knobPx, knobPx, knobPx / 2, knob.toHex()),
                knobPx, knobPx
            )
            knobCacheKey = knobKey
        }

        val knobX = if (value) globalX() + width - knobSize - 2f else globalX() + 2f
        val knobY = globalY() + height / 2f - knobRadius
        knobTexture?.let { ctx.scope.drawTexture(it, knobX, knobY, knobSize, knobSize, 1f) }



        renderChildren(ctx)
    }

    override fun onEvent(event: InputEvent): Boolean {
        when (event) {
            is MouseEvent.Click -> {
                if (!contains(event.x, event.y)) return false
                value = !value
                event.consume()
                return true
            }
        }
        return false
    }
}