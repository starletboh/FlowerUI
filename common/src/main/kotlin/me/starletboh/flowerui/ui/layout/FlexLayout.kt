package me.starletboh.flowerui.ui.layout

import me.starletboh.flowerui.ui.components.Component

/**
 * A single-axis flex layout, similar to CSS flexbox.
 *
 * @param direction Main axis: ROW (horizontal) or COLUMN (vertical).
 * @param justifyContent Distribution of children along the main axis.
 * @param alignItems Alignment of children along the cross axis
 * (STRETCH makes each child fill the cross-axis size of the parent).
 * @param wrap Only applies to ROW: wraps children onto new lines instead of
 * overflowing the available width.
 */
class FlexLayout(
    private val direction: FlexDirection,
    private val justifyContent: JustifyContent = JustifyContent.START,
    private val alignItems: AlignItems = AlignItems.START,
    private val wrap: Boolean = false
) : Layout {

    private fun delegate(): Layout = when (direction) {
        FlexDirection.ROW -> RowLayout(wrap = wrap, justify = justifyContent)
        FlexDirection.COLUMN -> ColumnLayout(justify = justifyContent)
    }

    private fun LayoutBox.withCrossAxisAlign(): LayoutBox {
        val mapped = when (alignItems) {
            AlignItems.START -> Align.START
            AlignItems.CENTER -> Align.CENTER
            AlignItems.END -> Align.END
            AlignItems.STRETCH -> Align.START
        }
        return if (direction == FlexDirection.ROW) copy(alignY = mapped) else copy(alignX = mapped)
    }

    override fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size {
        return delegate().measure(parent, box.withCrossAxisAlign(), availableWidth, availableHeight)
    }

    override fun arrange(parent: Component, box: LayoutBox) {

        delegate().arrange(parent, box.withCrossAxisAlign())

        if (alignItems == AlignItems.STRETCH) {
            val innerWidth = (parent.width - box.padding * 2).coerceAtLeast(0f)
            val innerHeight = (parent.height - box.padding * 2).coerceAtLeast(0f)

            for (child in parent.children) {
                when (direction) {
                    FlexDirection.ROW -> child.height = innerHeight
                    FlexDirection.COLUMN -> child.width = innerWidth
                }
            }
        }
    }
}