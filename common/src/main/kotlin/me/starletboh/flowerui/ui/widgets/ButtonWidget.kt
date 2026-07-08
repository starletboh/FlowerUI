package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.animation.EasingType
import me.starletboh.flowerui.animation.Tween
import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.animation.AnimationEngine
import me.starletboh.flowerui.theme.adapters.buttonStyle
import me.starletboh.flowerui.theme.style.ButtonStyle
import java.util.*
class ButtonWidget : Widget() {

    var text: String = ""
    var radius: Int = 6

    var backgroundColor: Int? = null
    var hoverColor: Int? = null
    var pressedColor: Int? = null
    var textColor: Int? = null
    var iconTexture: Any? = null
    var iconSize: Float = 16f
    var onClick: (() -> Unit)? = null

    private var hovered = false
    private var pressed = false

    private val hoverTween = Tween(0f)
    private val pressTween = Tween(0f)

    private var textureCacheKey: String? = null
    private var texture: Any? = null

    init {
        AnimationEngine.register(hoverTween)
        AnimationEngine.register(pressTween)
    }

    private fun resolveStyle(ctx: RenderContext): ButtonStyle {
        val base = ctx.theme.buttonStyle()

        return ButtonStyle(
            background = backgroundColor ?: base.background,
            hoverBackground = hoverColor ?: base.hoverBackground,
            pressedBackground = pressedColor ?: base.pressedBackground,
            textColor = textColor ?: base.textColor,
            borderColor = base.borderColor,
            radius = radius
        )
    }


    fun textureKey(vararg parts: Any?): String {
        val raw = parts.joinToString("|") { it.toString() }

        val hash = raw.hashCode().toUInt().toString(16)


        return "flowerui/ui_$hash"
    }
    override fun render(ctx: RenderContext) {





        val mx = ctx.mouseX
        val my = ctx.mouseY
        hovered = contains(mx, my)
        val style = resolveStyle(ctx)

        val svg = buildSvg(style)


        val key = textureKey(width, height, style.background, style.hoverBackground, style.pressedBackground, hovered, pressed, radius)
        if (key != textureCacheKey) {
            texture = SvgTextureManager.getSvgTexture(
                id = key,
                svg = svg,
                width = width.toInt().coerceAtLeast(1),
                height = height.toInt().coerceAtLeast(1)
            )
            textureCacheKey = key
        }

        texture?.let {
            ctx.scope.drawTexture(
                it,
                globalX(),
                globalY(),
                width,
                height,
                alpha = 1f
            )
        }


        iconTexture?.let { tex ->

            val hasText = text.isNotEmpty()

            val iconY = globalY() + (height - iconSize) / 2f

            val iconX = if (!hasText) {

                globalX() + (width - iconSize) / 2f
            } else {

                val textWidth = ctx.scope.measureTextWidth(text)
                val totalWidth = iconSize + 4f + textWidth

                globalX() + (width - totalWidth) / 2f
            }

            ctx.scope.drawTexture(
                tex,
                iconX,
                iconY,
                iconSize,
                iconSize,
                1f
            )
        }
        val hasText = text.isNotEmpty()

        if (hasText) {

            val textWidth = ctx.scope.measureTextWidth(text)
            val textHeight = ctx.scope.measureTextHeight(text)

            val iconOffset = iconSize + 4f

            val totalWidth = iconOffset + textWidth

            val startX = globalX() + (width - totalWidth) / 2f

            val textX = startX + iconOffset
            val textY = globalY() + (height - textHeight) / 2f

            ctx.scope.drawText(
                text,
                textX,
                textY,
                (textColor ?: 0xFFFFFFFF.toInt()),
                1f
            )
        }

        renderChildren(ctx)
    }

    private fun buildSvg(style: ButtonStyle): String {

        val fill = when {
            pressed -> style.pressedBackground
            hovered -> style.hoverBackground
            else -> style.background
        }

        return SvgBuilder.roundedRect(
            width = width.toInt().coerceAtLeast(1),
            height = height.toInt().coerceAtLeast(1),
            radius = style.radius,
            fill = fill.toHex()
        )
    }

    override fun onEvent(event: InputEvent): Boolean {
        when (event) {

            is MouseEvent.Move -> {
                hovered = contains(event.x, event.y)
            }

            is MouseEvent.Click -> {
                pressed = true
                pressTween.to(1f, 0.08f, EasingType.EASE_OUT)
                event.consume()
                return true
            }

            is MouseEvent.Release -> {
                val inside = contains(event.x, event.y)

                if (pressed && inside) {
                    onClick?.invoke()
                }

                pressed = false
                pressTween.to(0f, 0.12f, EasingType.EASE_OUT)
                return inside
            }
        }
        return false
    }


}