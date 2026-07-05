package me.starletboh.flowerui.ui.layout

import me.starletboh.flowerui.ui.components.Component

/**
 * Lays children out top-to-bottom.
 *
 * @param justify Main-axis (vertical) distribution of children within the
 * parent's height. Cross-axis (horizontal) alignment is controlled by
 * [LayoutBox.alignX]. Defaults to START, i.e. simple top-down stacking.
 */
class ColumnLayout(
    private val justify: JustifyContent = JustifyContent.START
) : Layout {

    override fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size {
        if (parent.children.isEmpty()) return Size(box.padding * 2, box.padding * 2)

        val contentWidth = parent.children.maxOf { it.width }
        val contentHeight = parent.children.sumOf { it.height.toDouble() }.toFloat() +
                box.gap * (parent.children.size - 1).coerceAtLeast(0)

        return Size(contentWidth + box.padding * 2, contentHeight + box.padding * 2)
    }

    override fun arrange(parent: Component, box: LayoutBox) {

        if (parent.children.isEmpty()) return

        val startX = box.startX + box.padding
        val startY = box.startY + box.padding
        val innerWidth = (parent.width - box.padding * 2).coerceAtLeast(0f)
        val maxHeight = (parent.height - box.padding * 2).coerceAtLeast(0f)

        val n = parent.children.size
        val contentHeight = parent.children.sumOf { it.height.toDouble() }.toFloat() +
                box.gap * (n - 1).coerceAtLeast(0)
        val available = (maxHeight - contentHeight).coerceAtLeast(0f)

        var cursorY = when (justify) {
            JustifyContent.START -> startY
            JustifyContent.CENTER -> startY + available / 2f
            JustifyContent.END -> startY + available
            JustifyContent.SPACE_BETWEEN, JustifyContent.SPACE_AROUND -> startY
        }

        val extraGap = when (justify) {
            JustifyContent.SPACE_BETWEEN -> if (n > 1) available / (n - 1) else 0f
            JustifyContent.SPACE_AROUND -> if (n > 0) available / n else 0f
            else -> 0f
        }

        if (justify == JustifyContent.SPACE_AROUND) cursorY += extraGap / 2f

        for (child in parent.children) {

            child.x = when (box.alignX) {
                Align.START -> startX
                Align.CENTER -> startX + (innerWidth - child.width) / 2f
                Align.END -> startX + innerWidth - child.width
            }

            child.y = cursorY
            cursorY += child.height + box.gap + extraGap
        }
    }
}