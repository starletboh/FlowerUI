package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.graphics.svg.generator.SvgBuilder
import me.starletboh.flowerui.input.InputRouter
import me.starletboh.flowerui.ui.components.Component
import me.starletboh.flowerui.ui.components.Overlay
import me.starletboh.flowerui.ui.render.RenderContext


class DropdownWidget<T> : Widget(), Overlay {

    var items: List<T> = emptyList()
    var selectedIndex: Int = -1

    var labelProvider: (T) -> String = { it.toString() }

    var isOpen = false
        private set

    var onChange: ((T?) -> Unit)? = null

    var radius: Int = 6
    var backgroundColor: Int? = null
    var hoveredRowColor: Int? = null

    val selectedItem: T?
        get() = items.getOrNull(selectedIndex)

    private var hoveredRow = -1

    private var boxCacheKey: String? = null
    private var boxTexture: Any? = null
    private var rowCacheKey: String? = null
    private var rowTexture: Any? = null





    override fun render(ctx: RenderContext) {


        if (isOpen && InputRouter.currentFocus !== this) {
            closeMenu()
        }

        val fill = backgroundColor ?: ctx.theme.colors.surface
        val key = "$width:$height:$fill:$radius"
        if (key != boxCacheKey) {
            boxTexture = SvgTextureManager.getSvgTexture(
                "dropdown_box:$key",
                SvgBuilder.roundedRect(width.toInt().coerceAtLeast(1), height.toInt().coerceAtLeast(1), radius, fill.toHex()),
                width.toInt().coerceAtLeast(1),
                height.toInt().coerceAtLeast(1)
            )
            boxCacheKey = key
        }

        boxTexture?.let { ctx.scope.drawTexture(it, globalX(), globalY(), width, height, 1f) }
        ctx.scope.drawOutline(globalX(), globalY(), width, height, ctx.theme.colors.border, 1f)

        val label = selectedItem?.let(labelProvider) ?: "Select..."
        val textY = globalY() + (height - ctx.scope.measureTextHeight(label)) / 2f
        ctx.scope.drawText(label, globalX() + 6f, textY, ctx.theme.colors.textPrimary, 1f)


        val caretSize = 5f
        val caretX = globalX() + width - caretSize - 8f
        val caretY = globalY() + height / 2f - caretSize / 4f
        ctx.scope.drawRect(caretX, caretY, caretSize, caretSize * 0.5f, ctx.theme.colors.textSecondary)

        renderChildren(ctx)
    }






    override fun renderOverlay(ctx: RenderContext) {
        if (!isOpen || items.isEmpty()) return

        val itemH = height
        val listHeight = itemH * items.size
        val listY = globalY() + height

        val fill = backgroundColor ?: ctx.theme.colors.surface
        val key = "$width:$listHeight:$fill:$radius"
        if (key != rowCacheKey) {
            rowTexture = SvgTextureManager.getSvgTexture(
                "dropdown_list:$key",
                SvgBuilder.roundedRect(width.toInt().coerceAtLeast(1), listHeight.toInt().coerceAtLeast(1), radius, fill.toHex()),
                width.toInt().coerceAtLeast(1),
                listHeight.toInt().coerceAtLeast(1)
            )
            rowCacheKey = key
        }

        rowTexture?.let { ctx.scope.drawTexture(it, globalX(), listY, width, listHeight, 1f) }
        ctx.scope.drawOutline(globalX(), listY, width, listHeight, ctx.theme.colors.border, 1f)

        for (i in items.indices) {
            val y = listY + i * itemH

            if (i == hoveredRow) {
                ctx.scope.drawRect(globalX(), y, width, itemH, hoveredRowColor ?: ctx.theme.colors.hover)
            }

            val label = labelProvider(items[i])
            val textY = y + (itemH - ctx.scope.measureTextHeight(label)) / 2f
            ctx.scope.drawText(label, globalX() + 6f, textY, ctx.theme.colors.textPrimary, 1f)
        }
    }

    override fun overlayHitTest(x: Double, y: Double): Component? {
        return if (isOpen && rowIndexAt(x, y) >= 0) this else null
    }

    private fun rowIndexAt(px: Double, py: Double): Int {
        if (!isOpen) return -1
        val itemH = height
        val listY = globalY() + height
        for (i in items.indices) {
            val y = listY + i * itemH
            if (px >= globalX() && px <= globalX() + width && py >= y && py <= y + itemH) return i
        }
        return -1
    }





    override fun onEvent(event: InputEvent): Boolean {
        when (event) {

            is MouseEvent.Move -> {
                hoveredRow = rowIndexAt(event.x, event.y)
                return false
            }

            is MouseEvent.Click -> {

                if (!isOpen) {
                    if (!contains(event.x, event.y)) return false
                    openMenu()
                    event.consume()
                    return true
                }


                val idx = rowIndexAt(event.x, event.y)
                if (idx >= 0) {
                    selectedIndex = idx
                    onChange?.invoke(selectedItem)
                    closeMenu()
                    event.consume()
                    return true
                }

                if (contains(event.x, event.y)) {

                    closeMenu()
                    event.consume()
                    return true
                }

                return false
            }
        }
        return false
    }

    private fun openMenu() {
        isOpen = true
        findRoot()?.showOverlay(this)
    }

    private fun closeMenu() {
        isOpen = false
        hoveredRow = -1
        findRoot()?.hideOverlay(this)
    }
}