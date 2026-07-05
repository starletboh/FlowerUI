package me.starletboh.flowerui.ui.layout

import me.starletboh.flowerui.ui.components.Component
import kotlin.math.max

/**
 * Arranges children into a grid.
 *
 * @param columns Number of columns. Pass 0 (or omit) to auto-fit as many
 * columns as will fit the available width, based on the first child's width.
 * @param alignItems Vertical alignment of a child within its row's height.
 * @param justifyItems Horizontal alignment of a child within its column's
 * width.
 * @param stretch When true, every child's width is stretched to fill its
 * column (and, combined with [alignItems] = STRETCH, its height fills the
 * row).
 */
class GridLayout(
    private val columns: Int = 0,
    private val alignItems: AlignItems = AlignItems.START,
    private val justifyItems: Align = Align.START,
    private val stretch: Boolean = false
) : Layout {

    private fun resolveColumns(children: List<Component>, availableWidth: Float, gap: Float): Int {
        if (columns > 0) return columns
        if (children.isEmpty()) return 1
        val childWidth = children.first().width.coerceAtLeast(1f)
        if (!availableWidth.isFinite()) return children.size
        return max(1, ((availableWidth + gap) / (childWidth + gap)).toInt())
    }

    private fun rowHeights(children: List<Component>, cols: Int): Map<Int, Float> {
        val rows = mutableMapOf<Int, Float>()
        for ((i, child) in children.withIndex()) {
            val row = i / cols
            rows[row] = maxOf(rows[row] ?: 0f, child.height)
        }
        return rows
    }

    override fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size {
        if (parent.children.isEmpty()) return Size(box.padding * 2, box.padding * 2)

        val innerAvailable = if (availableWidth.isFinite()) (availableWidth - box.padding * 2) else availableWidth
        val cols = resolveColumns(parent.children, innerAvailable, box.gap)
        val rows = rowHeights(parent.children, cols)

        val widestCol = parent.children.maxOf { it.width }
        val contentWidth = cols * widestCol + (cols - 1).coerceAtLeast(0) * box.gap
        val contentHeight = rows.values.sum() + (rows.size - 1).coerceAtLeast(0) * box.gap

        return Size(contentWidth + box.padding * 2, contentHeight + box.padding * 2)
    }

    override fun arrange(parent: Component, box: LayoutBox) {

        if (parent.children.isEmpty()) return

        val availableWidth = (parent.width - box.padding * 2).coerceAtLeast(0f)
        val cols = resolveColumns(parent.children, availableWidth, box.gap)

        val totalGap = box.gap * (cols - 1)
        val cellWidth = ((availableWidth - totalGap) / cols).coerceAtLeast(0f)

        // Row heights, based on each child's natural height before stretch.
        val rowHeightMap = rowHeights(parent.children, cols)

        for ((i, child) in parent.children.withIndex()) {

            val col = i % cols
            val row = i / cols
            val rowHeight = rowHeightMap[row] ?: child.height

            val cellX = box.startX + box.padding + col * (cellWidth + box.gap)
            var cellY = box.startY + box.padding
            for (r in 0 until row) {
                cellY += (rowHeightMap[r] ?: 0f) + box.gap
            }

            if (stretch) child.width = cellWidth
            if (stretch && alignItems == AlignItems.STRETCH) child.height = rowHeight

            child.x = cellX + when (justifyItems) {
                Align.START -> 0f
                Align.CENTER -> (cellWidth - child.width) / 2f
                Align.END -> cellWidth - child.width
            }

            child.y = cellY + when (alignItems) {
                AlignItems.START -> 0f
                AlignItems.CENTER -> (rowHeight - child.height) / 2f
                AlignItems.END -> rowHeight - child.height
                AlignItems.STRETCH -> 0f
            }
        }
    }
}