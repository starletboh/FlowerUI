package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.KeyEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.input.InputRouter
import me.starletboh.flowerui.input.KeyCodes
import me.starletboh.flowerui.ref.PlatformServices
import me.starletboh.flowerui.ui.render.RenderContext


class TextInputWidget : Widget() {

    var text: String = ""
        set(value) {
            val clamped = if (value.length > maxLength)
                value.substring(0, maxLength)
            else value

            if (field == clamped) return
            field = clamped

            clamp()
            anchor = caret

            onChange?.invoke(field)
        }

    var placeholder: String = ""
    var maxLength: Int = 256
    var radius: Int = 6

    var backgroundColor: Int? = null
    var focusedBorderColor: Int? = null
    var textColor: Int? = null
    var placeholderColor: Int? = null

    var onChange: ((String) -> Unit)? = null
    var onSubmit: ((String) -> Unit)? = null

    private var caret = 0
    private var anchor = 0
    private val modSHIFT = 1
    private val modCONTROL = 2
    private var textureCacheKey: String? = null
    private var texture: Any? = null

    private val isFocused: Boolean
        get() = InputRouter.currentFocus === this





    private fun hasSelection() = caret != anchor
    private fun selectionStart() = minOf(caret, anchor)
    private fun selectionEnd() = maxOf(caret, anchor)

    private fun clamp() {
        caret = caret.coerceIn(0, text.length)
        anchor = anchor.coerceIn(0, text.length)
    }





    override fun render(ctx: RenderContext) {

        val focused = isFocused
        val base = backgroundColor ?: ctx.theme.colors.surface

        val key = "$width:$height:$base:$radius"

        if (key != textureCacheKey) {
            texture = SvgTextureManager.getSvgTexture(
                id = key,
                svg = SvgBuilder.roundedRect(
                    width.toInt().coerceAtLeast(1),
                    height.toInt().coerceAtLeast(1),
                    radius,
                    base.toHex()
                ),
                width = width.toInt(),
                height = height.toInt()
            )
            textureCacheKey = key
        }

        texture?.let {
            ctx.scope.drawTexture(it, globalX(), globalY(), width, height, 1f)
        }

        val border = if (focused)
            (focusedBorderColor ?: ctx.theme.colors.primary)
        else ctx.theme.colors.border

        ctx.scope.drawOutline(globalX(), globalY(), width, height, border, if (focused) 1.5f else 1f)

        val padding = 4f
        val textHeight = ctx.scope.measureTextHeight(text)
        val textY = globalY() + (height - textHeight) / 2f

        val safeCaret = caret.coerceIn(0, text.length)





        if (focused && hasSelection()) {
            val start = selectionStart()
            val end = selectionEnd()

            val x1 = ctx.scope.measureTextWidth(text.substring(0, start))
            val w = ctx.scope.measureTextWidth(text.substring(start, end))

            ctx.scope.drawRect(
                globalX() + padding + x1,
                textY,
                w,
                textHeight.coerceAtLeast(8f),
                ctx.theme.colors.primary
            )
        }





        if (text.isEmpty()) {
            if (!focused && placeholder.isNotEmpty()) {
                ctx.scope.drawText(
                    placeholder,
                    globalX() + padding,
                    textY,
                    placeholderColor ?: ctx.theme.colors.textSecondary,
                    1f
                )
            }
        } else {
            ctx.scope.drawText(
                text,
                globalX() + padding,
                textY,
                textColor ?: ctx.theme.colors.textPrimary,
                1f
            )
        }





        if (focused && (ctx.time / 500L) % 2L == 0L) {
            val cx = globalX() + padding +
                    ctx.scope.measureTextWidth(text.substring(0, safeCaret))

            ctx.scope.drawRect(
                cx,
                textY,
                1f,
                textHeight.coerceAtLeast(8f),
                textColor ?: ctx.theme.colors.textPrimary
            )
        }

        renderChildren(ctx)
    }






    private fun isShift(mod: Int): Boolean =
        (mod and modSHIFT) != 0

    private fun isCtrl(mod: Int): Boolean =
        (mod and modCONTROL) != 0
    override fun onEvent(event: InputEvent): Boolean {

        when (event) {

            is MouseEvent.Click -> {
                if (!contains(event.x, event.y)) return false
                caret = text.length
                anchor = caret
                event.consume()
                return true
            }

            is KeyEvent.CharTyped -> {
                if (!isFocused) return false
                insert(event.char)
                event.consume()
                return true
            }

            is KeyEvent.Press -> {
                if (!isFocused) return false

                val ctrl = isCtrl(event.modifiers)
                val shift = isShift(event.modifiers)

                when (event.keyCode) {

                    KeyCodes.KEY_A -> {
                        if (ctrl) {
                            caret = text.length
                            anchor = 0
                            event.consume()
                            return true
                        }
                    }

                    KeyCodes.KEY_C -> {
                        if (ctrl) {
                            copy()
                            event.consume()
                            return true
                        }
                    }

                    KeyCodes.KEY_V -> {
                        if (ctrl) {
                            paste()
                            event.consume()
                            return true
                        }
                    }

                    KeyCodes.KEY_LEFT -> {
                        if (shift) {
                            caret = (caret - 1).coerceAtLeast(0)
                        } else {
                            caret = (caret - 1).coerceAtLeast(0)
                            anchor = caret
                        }
                    }

                    KeyCodes.KEY_RIGHT -> {
                        if (shift) {
                            caret = (caret + 1).coerceAtMost(text.length)
                        } else {
                            caret = (caret + 1).coerceAtMost(text.length)
                            anchor = caret
                        }
                    }

                    KeyCodes.KEY_BACKSPACE -> backspace()
                    KeyCodes.KEY_DELETE -> delete()

                    KeyCodes.KEY_ENTER,
                    KeyCodes.KEY_KP_ENTER -> onSubmit?.invoke(text)

                    else -> return false
                }

                event.consume()
                return true
            }
        }

        return false
    }





    private fun insert(char: Char) {
        if (char.isISOControl()) return
        if (text.length >= maxLength && !hasSelection()) return

        val start = selectionStart()
        val end = selectionEnd()

        text = if (hasSelection()) {
            text.substring(0, start) + char + text.substring(end)
        } else {
            text.substring(0, caret) + char + text.substring(caret)
        }

        caret = start + 1
        anchor = caret
    }

    private fun backspace() {
        if (hasSelection()) {
            val s = selectionStart()
            val e = selectionEnd()
            text = text.substring(0, s) + text.substring(e)
            caret = s
            anchor = s
            return
        }

        if (caret == 0) return

        text = text.substring(0, caret - 1) + text.substring(caret)
        caret--
        anchor = caret
    }

    private fun delete() {
        if (hasSelection()) {
            val s = selectionStart()
            val e = selectionEnd()
            text = text.substring(0, s) + text.substring(e)
            caret = s
            anchor = s
            return
        }

        if (caret >= text.length) return

        text = text.substring(0, caret) + text.substring(caret + 1)
    }





    private fun copy() {
        if (!hasSelection()) return

        val s = selectionStart()
        val e = selectionEnd()

        PlatformServices.clipboard.set(text.substring(s, e))
    }

    private fun paste() {
        val clip = PlatformServices.clipboard.get() ?: return

        val s = selectionStart()
        val e = selectionEnd()

        text = if (hasSelection()) {
            text.substring(0, s) + clip + text.substring(e)
        } else {
            text.substring(0, caret) + clip + text.substring(caret)
        }

        caret = s + clip.length
        anchor = caret
    }
}