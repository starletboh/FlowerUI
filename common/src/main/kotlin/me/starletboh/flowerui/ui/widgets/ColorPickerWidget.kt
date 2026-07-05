package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * A full color picker: saturation/value square + hue strip + alpha slider +
 * preview swatch, plus a row of quick-pick palette swatches underneath.
 *
 * Reads/writes ARGB ints via [color]/[setColor] - alpha lives in the top
 * byte (`0xAARRGGBB`), so this covers "any color" *and* transparency.
 *
 * Self-sizing: don't set width/height, they're derived from the other
 * size fields ([squareSize], [barThickness], etc).
 */
class ColorPickerWidget(
    private val palette: List<Int> = defaultPalette
) : Widget() {

    var onColorChanged: ((Int) -> Unit)? = null

    var squareSize: Float = 90f
    var barThickness: Float = 12f
    var previewSize: Float = 18f
    var gap: Float = 6f
    var swatchSize: Float = 12f
    var swatchGap: Float = 3f

    // HSV(A), all 0..1 - the source of truth. `color` is derived from these.
    private var hue = 0f
    private var sat = 0f
    private var bri = 1f
    private var alpha = 1f

    /** Current selection as 0xAARRGGBB. */
    val color: Int
        get() {
            val rgb = java.awt.Color.HSBtoRGB(hue, sat, bri)
            val a = (alpha * 255f).roundToInt().coerceIn(0, 255)
            return (a shl 24) or (rgb and 0xFFFFFF)
        }

    /** Sets the picker's state from an ARGB int without firing [onColorChanged]. */
    fun setColor(argb: Int) {
        val a = (argb ushr 24) and 0xFF
        val r = (argb shr 16) and 0xFF
        val g = (argb shr 8) and 0xFF
        val b = argb and 0xFF

        val hsb = java.awt.Color.RGBtoHSB(r, g, b, null)
        hue = hsb[0]
        sat = hsb[1]
        bri = hsb[2]
        alpha = a / 255f
    }

    private var draggingSquare = false
    private var draggingHue = false
    private var draggingAlpha = false
    private var hoveredSwatch = -1

    private var squareCacheKey: String? = null
    private var squareTexture: Any? = null
    private var hueTexture: Any? = null
    private var alphaCacheKey: String? = null
    private var alphaTexture: Any? = null
    private var checkerTexture: Any? = null

    companion object {
        val defaultPalette = listOf(
            0xFFFFFFFF.toInt(), 0xFF9E9E9E.toInt(), 0xFF000000.toInt(),
            0xFFE57373.toInt(), 0xFFFFB74D.toInt(), 0xFFFFF176.toInt(),
            0xFF81C784.toInt(), 0xFF4FC3F7.toInt(), 0xFF7986CB.toInt(),
            0xFFBA68C8.toInt(), 0xFFF06292.toInt(), 0xFFA1887F.toInt()
        )

        /** Rounds a 0..1 value to a fixed step count, keeping SV/alpha texture cache sizes bounded during drags. */
        private fun quantize(v: Float, steps: Int): Float = (v * steps).roundToInt() / steps.toFloat()
    }

    // ------------------------------------------------------------------
    // layout (self-sizing)
    // ------------------------------------------------------------------

    private val totalWidth: Float
        get() = squareSize + gap + barThickness + gap + previewSize

    private fun swatchColumns(): Int =
        max(1, ((totalWidth + swatchGap) / (swatchSize + swatchGap)).toInt())

    private fun swatchRows(): Int {
        val cols = swatchColumns()
        return (palette.size + cols - 1) / cols
    }

    private val squareX get() = globalX()
    private val squareY get() = globalY()
    private val hueBarX get() = globalX() + squareSize + gap
    private val hueBarY get() = globalY()
    private val previewX get() = hueBarX + barThickness + gap
    private val previewY get() = globalY()
    private val alphaBarX get() = globalX()
    private val alphaBarY get() = globalY() + squareSize + gap
    private val swatchesY get() = alphaBarY + barThickness + gap

    // ------------------------------------------------------------------
    // render
    // ------------------------------------------------------------------

    override fun render(ctx: RenderContext) {

        val rows = swatchRows()
        width = totalWidth
        height = squareSize + gap + barThickness + gap +
                rows * swatchSize + (rows - 1).coerceAtLeast(0) * swatchGap

        drawSquare(ctx)
        drawHueBar(ctx)
        drawPreview(ctx)
        drawAlphaBar(ctx)
        drawSwatches(ctx)

        renderChildren(ctx)
    }

    private fun ensureChecker(ctx: RenderContext, w: Int, h: Int): Any? {
        val key = "colorpicker_checker:$w:$h"
        if (checkerTexture == null) {
            checkerTexture = SvgTextureManager.getSvgTexture(key, SvgBuilder.checkerboard(w, h), w, h)
        }
        return checkerTexture
    }

    private fun drawSquare(ctx: RenderContext) {
        val w = squareSize.toInt().coerceAtLeast(1)
        val h = squareSize.toInt().coerceAtLeast(1)

        // Quantize hue so a drag across the hue bar doesn't generate a
        // brand-new full-square raster on every single pixel of movement.
        val qHue = quantize(hue, 60)
        val key = "sv:$w:$h:$qHue"

        if (key != squareCacheKey) {
            val hueHex = String.format("#%06X", java.awt.Color.HSBtoRGB(qHue, 1f, 1f) and 0xFFFFFF)
            squareTexture = SvgTextureManager.getSvgTexture(key, SvgBuilder.saturationValueSquare(w, h, hueHex), w, h)
            squareCacheKey = key
        }

        squareTexture?.let { ctx.scope.drawTexture(it, squareX, squareY, squareSize, squareSize, 1f) }

        // selection marker
        val markerX = squareX + sat * squareSize
        val markerY = squareY + (1f - bri) * squareSize
        ctx.scope.drawOutline(markerX - 3f, markerY - 3f, 6f, 6f, 0xFFFFFFFF.toInt(), 1.5f)

        ctx.scope.drawOutline(squareX, squareY, squareSize, squareSize, ctx.theme.colors.border, 1f)
    }

    private fun drawHueBar(ctx: RenderContext) {
        val w = barThickness.toInt().coerceAtLeast(1)
        val h = squareSize.toInt().coerceAtLeast(1)

        if (hueTexture == null) {
            // constant regardless of state - built once and reused forever
            hueTexture = SvgTextureManager.getSvgTexture("huebar:$w:$h", SvgBuilder.hueBarVertical(w, h), w, h)
        }

        hueTexture?.let { ctx.scope.drawTexture(it, hueBarX, hueBarY, barThickness, squareSize, 1f) }

        val markerY = hueBarY + hue * squareSize
        ctx.scope.drawOutline(hueBarX - 1f, markerY - 2f, barThickness + 2f, 4f, 0xFFFFFFFF.toInt(), 1.5f)
        ctx.scope.drawOutline(hueBarX, hueBarY, barThickness, squareSize, ctx.theme.colors.border, 1f)
    }

    private fun drawPreview(ctx: RenderContext) {
        val w = previewSize.toInt().coerceAtLeast(1)
        val h = squareSize.toInt().coerceAtLeast(1)

        ensureChecker(ctx, w, h)?.let { ctx.scope.drawTexture(it, previewX, previewY, previewSize, squareSize, 1f) }
        ctx.scope.drawRect(previewX, previewY, previewSize, squareSize, color)
        ctx.scope.drawOutline(previewX, previewY, previewSize, squareSize, ctx.theme.colors.border, 1f)
    }

    private fun drawAlphaBar(ctx: RenderContext) {
        val w = totalWidth.toInt().coerceAtLeast(1)
        val h = barThickness.toInt().coerceAtLeast(1)

        ensureChecker(ctx, w, h)?.let { ctx.scope.drawTexture(it, alphaBarX, alphaBarY, totalWidth, barThickness, 1f) }

        val qRgb = java.awt.Color.HSBtoRGB(quantize(hue, 60), quantize(sat, 40), quantize(bri, 40)) and 0xFFFFFF
        val key = "alpha:$w:$h:$qRgb"
        if (key != alphaCacheKey) {
            val hex = String.format("#%06X", qRgb)
            alphaTexture = SvgTextureManager.getSvgTexture(key, SvgBuilder.alphaBarHorizontal(w, h, hex), w, h)
            alphaCacheKey = key
        }

        alphaTexture?.let { ctx.scope.drawTexture(it, alphaBarX, alphaBarY, totalWidth, barThickness, 1f) }

        val markerX = alphaBarX + alpha * totalWidth
        ctx.scope.drawOutline(markerX - 2f, alphaBarY - 1f, 4f, barThickness + 2f, 0xFFFFFFFF.toInt(), 1.5f)
        ctx.scope.drawOutline(alphaBarX, alphaBarY, totalWidth, barThickness, ctx.theme.colors.border, 1f)
    }

    private fun drawSwatches(ctx: RenderContext) {
        val cols = swatchColumns()
        for ((i, c) in palette.withIndex()) {
            val col = i % cols
            val row = i / cols
            val x = globalX() + col * (swatchSize + swatchGap)
            val y = swatchesY + row * (swatchSize + swatchGap)

            ctx.scope.drawRect(x, y, swatchSize, swatchSize, c)

            if (i == hoveredSwatch) {
                ctx.scope.drawOutline(x, y, swatchSize, swatchSize, ctx.theme.colors.textSecondary, 1f)
            }
        }
    }

    // ------------------------------------------------------------------
    // input
    // ------------------------------------------------------------------

    override fun onEvent(event: InputEvent): Boolean {
        when (event) {

            is MouseEvent.Click -> {
                if (inSquare(event.x, event.y)) {
                    draggingSquare = true
                    updateFromSquare(event.x, event.y)
                    event.consume()
                    return true
                }
                if (inHueBar(event.x, event.y)) {
                    draggingHue = true
                    updateFromHueBar(event.y)
                    event.consume()
                    return true
                }
                if (inAlphaBar(event.x, event.y)) {
                    draggingAlpha = true
                    updateFromAlphaBar(event.x)
                    event.consume()
                    return true
                }
                val idx = swatchIndexAt(event.x, event.y)
                if (idx >= 0) {
                    setColor(palette[idx])
                    onColorChanged?.invoke(color)
                    event.consume()
                    return true
                }
                return false
            }

            is MouseEvent.Move -> {
                hoveredSwatch = swatchIndexAt(event.x, event.y)

                if (draggingSquare) {
                    updateFromSquare(event.x, event.y)
                    return true
                }
                if (draggingHue) {
                    updateFromHueBar(event.y)
                    return true
                }
                if (draggingAlpha) {
                    updateFromAlphaBar(event.x)
                    return true
                }
                return false
            }

            is MouseEvent.Release -> {
                if (draggingSquare || draggingHue || draggingAlpha) {
                    draggingSquare = false
                    draggingHue = false
                    draggingAlpha = false
                    event.consume()
                    return true
                }
                return false
            }
        }
        return false
    }

    private fun inSquare(px: Double, py: Double) =
        px >= squareX && px <= squareX + squareSize && py >= squareY && py <= squareY + squareSize

    private fun inHueBar(px: Double, py: Double) =
        px >= hueBarX && px <= hueBarX + barThickness && py >= hueBarY && py <= hueBarY + squareSize

    private fun inAlphaBar(px: Double, py: Double) =
        px >= alphaBarX && px <= alphaBarX + totalWidth && py >= alphaBarY && py <= alphaBarY + barThickness

    private fun swatchIndexAt(px: Double, py: Double): Int {
        val cols = swatchColumns()
        for (i in palette.indices) {
            val col = i % cols
            val row = i / cols
            val x = globalX() + col * (swatchSize + swatchGap)
            val y = swatchesY + row * (swatchSize + swatchGap)
            if (px >= x && px <= x + swatchSize && py >= y && py <= y + swatchSize) return i
        }
        return -1
    }

    private fun updateFromSquare(px: Double, py: Double) {
        sat = (((px - squareX) / squareSize).toFloat()).coerceIn(0f, 1f)
        bri = (1f - ((py - squareY) / squareSize).toFloat()).coerceIn(0f, 1f)
        onColorChanged?.invoke(color)
    }

    private fun updateFromHueBar(py: Double) {
        hue = (((py - hueBarY) / squareSize).toFloat()).coerceIn(0f, 1f)
        onColorChanged?.invoke(color)
    }

    private fun updateFromAlphaBar(px: Double) {
        alpha = (((px - alphaBarX) / totalWidth).toFloat()).coerceIn(0f, 1f)
        onColorChanged?.invoke(color)
    }
}