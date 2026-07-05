package me.starletboh.flowerui.ui.layout

import me.starletboh.flowerui.ui.components.Component

/**
 * Lays children out left-to-right.
 *
 * @param wrap When true, children that would overflow the available width
 * flow onto a new line instead of overflowing it.
 * @param justify Main-axis (horizontal) distribution of children on each
 * line. Cross-axis (vertical) alignment is controlled by [LayoutBox.alignY].
 */
class RowLayout(
    private val wrap: Boolean = false,
    private val justify: JustifyContent = JustifyContent.START
) : Layout {

    /** Groups children into wrapped lines. With wrap = false this is always one line. */
    private fun computeLines(children: List<Component>, gap: Float, maxWidth: Float): List<List<Component>> {
        if (children.isEmpty()) return emptyList()

        val lines = mutableListOf<MutableList<Component>>()
        var current = mutableListOf<Component>()
        var lineWidth = 0f

        for (child in children) {
            val added = if (current.isEmpty()) child.width else gap + child.width

            if (wrap && current.isNotEmpty() && maxWidth.isFinite() && lineWidth + added > maxWidth) {
                lines += current
                current = mutableListOf()
                lineWidth = 0f
            }

            current += child
            lineWidth += if (current.size == 1) child.width else gap + child.width
        }
        if (current.isNotEmpty()) lines += current

        return lines
    }

    override fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size {
        val innerAvailable = if (availableWidth.isFinite()) (availableWidth - box.padding * 2).coerceAtLeast(0f) else Float.MAX_VALUE
        val lines = computeLines(parent.children, box.gap, innerAvailable)

        if (lines.isEmpty()) return Size(box.padding * 2, box.padding * 2)

        var contentWidth = 0f
        var contentHeight = 0f

        for (line in lines) {
            val lineWidth = line.sumOf { it.width.toDouble() }.toFloat() + box.gap * (line.size - 1)
            contentWidth = maxOf(contentWidth, lineWidth)
            contentHeight += (line.maxOf { it.height }) + box.gap
        }
        contentHeight -= box.gap // remove trailing gap from the last line

        return Size(contentWidth + box.padding * 2, contentHeight + box.padding * 2)
    }

    override fun arrange(parent: Component, box: LayoutBox) {

        if (parent.children.isEmpty()) return

        val startX = box.startX + box.padding
        val startY = box.startY + box.padding
        val maxWidth = (parent.width - box.padding * 2).coerceAtLeast(0f)

        val lines = computeLines(parent.children, box.gap, maxWidth)

        var cursorY = startY

        for (line in lines) {

            val lineHeight = line.maxOf { it.height }
            val contentWidth = line.sumOf { it.width.toDouble() }.toFloat() +
                    box.gap * (line.size - 1).coerceAtLeast(0)
            val available = (maxWidth - contentWidth).coerceAtLeast(0f)

            var cursorX = when (justify) {
                JustifyContent.START -> startX
                JustifyContent.CENTER -> startX + available / 2f
                JustifyContent.END -> startX + available
                JustifyContent.SPACE_BETWEEN, JustifyContent.SPACE_AROUND -> startX
            }

            val extraGap = when (justify) {
                JustifyContent.SPACE_BETWEEN -> if (line.size > 1) available / (line.size - 1) else 0f
                JustifyContent.SPACE_AROUND -> if (line.isNotEmpty()) available / line.size else 0f
                else -> 0f
            }

            if (justify == JustifyContent.SPACE_AROUND) cursorX += extraGap / 2f

            for (child in line) {

                val y = when (box.alignY) {
                    Align.START -> cursorY
                    Align.CENTER -> cursorY + (lineHeight - child.height) / 2f
                    Align.END -> cursorY + lineHeight - child.height
                }

                child.x = cursorX
                child.y = y

                cursorX += child.width + box.gap + extraGap
            }

            cursorY += lineHeight + box.gap
        }
    }
}