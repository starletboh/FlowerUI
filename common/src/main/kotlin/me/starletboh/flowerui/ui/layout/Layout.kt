package me.starletboh.flowerui.ui.layout

import me.starletboh.flowerui.ui.components.Component

/** A resolved width/height pair, as produced by [Layout.measure]. */
data class Size(val width: Float, val height: Float)

/**
 * A Layout is responsible for two distinct things - this is what the old
 * single-`apply()`-method version didn't actually have, which is why every
 * container needed a hand-set width/height or content would mis-place or
 * overflow it:
 *
 * 1. [measure] - "how big does [parent] need to be to fit its children?"
 *    Only used when [parent] doesn't already have an explicit width/height
 *    (i.e. it's meant to wrap its content, like a Row of buttons with no
 *    fixed size set). Children are measured bottom-up (see
 *    [Component.applyLayout]) so their own width/height are already final
 *    by the time this runs.
 *
 * 2. [arrange] - "given parent's *final* width/height, where does each
 *    child go?" This is what actually sets `child.x` / `child.y` (and, for
 *    stretch/fill behavior, `child.width` / `child.height`).
 */
interface Layout {
    fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size
    fun arrange(parent: Component, box: LayoutBox)
}