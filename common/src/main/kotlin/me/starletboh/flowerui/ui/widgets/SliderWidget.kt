package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext

/** A draggable numeric slider, rendered as a rounded-pill track/fill with a circular knob. */
class SliderWidget : Widget() {

    var min: Float = 0f
    var max: Float = 1f

    var value: Float = 0f
        set(v) {
            val clamped = v.coerceIn(min, max)
            if (field == clamped) return
            field = clamped
            onChange?.invoke(field)
        }

    var onChange: ((Float) -> Unit)? = null

    var trackColor: Int? = null
    var fillColor: Int? = null
    var knobColor: Int? = null
    var knobRadius: Float = 6f

    private var dragging = false

    private var trackCacheKey: String? = null
    private var trackTexture: Any? = null
    private var fillCacheKey: String? = null
    private var fillTexture: Any? = null
    private var knobCacheKey: String? = null
    private var knobTexture: Any? = null

    private fun normalized(): Float {
        if (max <= min) return 0f
        return (value - min) / (max - min)
    }

    private fun setFromMouse(px: Double) {
        val t = ((px - globalX()) / width).toFloat().coerceIn(0f, 1f)
        value = min + t * (max - min)
    }

    override fun render(ctx: RenderContext) {

        val barHeight = (height * 0.4f).coerceAtLeast(4f)
        val barY = globalY() + (height - barHeight) / 2f
        val barW = width.toInt().coerceAtLeast(1)
        val barH = barHeight.toInt().coerceAtLeast(1)
        val barRadius = (barHeight / 2f).toInt().coerceAtLeast(1)

        // track (full width, rounded pill)
        val trackFill = trackColor ?: ctx.theme.colors.border
        val trackKey = "$barW:$barH:$trackFill:$barRadius"
        if (trackKey != trackCacheKey) {
            trackTexture = SvgTextureManager.getSvgTexture(
                "slider_track:$trackKey",
                SvgBuilder.roundedRect(barW, barH, barRadius, trackFill.toHex()),
                barW, barH
            )
            trackCacheKey = trackKey
        }
        trackTexture?.let { ctx.scope.drawTexture(it, globalX(), barY, width, barHeight, 1f) }

        // fill: draw the same full-width pill shape but clipped to the
        // filled portion, so the visible left edge naturally follows the
        // track's rounding without needing separate partial-round math.
        val fill = fillColor ?: ctx.theme.colors.primary
        val fillKey = "$barW:$barH:$fill:$barRadius"
        if (fillKey != fillCacheKey) {
            fillTexture = SvgTextureManager.getSvgTexture(
                "slider_fill:$fillKey",
                SvgBuilder.roundedRect(barW, barH, barRadius, fill.toHex()),
                barW, barH
            )
            fillCacheKey = fillKey
        }

        val fillW = width * normalized()
        if (fillW > 0f) {
            ctx.scope.pushClip(globalX(), barY, fillW, barHeight)
            fillTexture?.let { ctx.scope.drawTexture(it, globalX(), barY, width, barHeight, 1f) }
            ctx.scope.popClip()
        }

        // knob (circle)
        val knobSize = knobRadius * 2f
        val knobPx = knobSize.toInt().coerceAtLeast(1)
        val knob = knobColor ?: 0xFFFFFFFF.toInt()
        val knobKey = "$knobPx:$knob"
        if (knobKey != knobCacheKey) {
            knobTexture = SvgTextureManager.getSvgTexture(
                "slider_knob:$knobKey",
                SvgBuilder.roundedRect(knobPx, knobPx, knobPx / 2, knob.toHex()),
                knobPx, knobPx
            )
            knobCacheKey = knobKey
        }

        val knobX = globalX() + fillW - knobRadius
        val knobY = globalY() + height / 2f - knobRadius
        knobTexture?.let { ctx.scope.drawTexture(it, knobX, knobY, knobSize, knobSize, 1f) }

        renderChildren(ctx)
    }

    override fun onEvent(event: InputEvent): Boolean {
        when (event) {

            is MouseEvent.Click -> {
                if (!contains(event.x, event.y)) return false
                dragging = true
                setFromMouse(event.x)
                event.consume()
                return true
            }

            is MouseEvent.Move -> {
                if (!dragging) return false
                setFromMouse(event.x)
                return true
            }

            is MouseEvent.Release -> {
                dragging = false
                return false
            }
        }
        return false
    }
}