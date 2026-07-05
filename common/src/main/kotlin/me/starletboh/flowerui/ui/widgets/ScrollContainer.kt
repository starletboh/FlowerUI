package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.ui.components.Component
import me.starletboh.flowerui.ui.render.RenderContext
import kotlin.math.max

/**
 * A clipping container that vertically scrolls its children.
 *
 * Usage: give it a fixed [width]/[height] (it deliberately does NOT
 * auto-size to its content - that would defeat the point of scrolling),
 * optionally set [layout] + [layoutBox] (e.g. a ColumnLayout) so children
 * stack naturally, then add children whose combined height exceeds the
 * container height.
 */
class ScrollContainer : Component() {

    var scrollY = 0f
        private set
    var maxScrollY = 0f
        private set

    /** Width (px) of the scrollbar track drawn on the right edge. */
    var scrollbarWidth: Float = 4f
    var scrollbarColor: Int = 0x66FFFFFF
    var trackColor: Int? = 0x22000000

    init {
        // Reuse Component's own clip mechanism instead of a separate
        // hand-rolled scissor call - one clipping code path for the whole
        // framework instead of two that can drift apart.
        clipChildren = true
    }

    override fun applyLayout() {

        // 1) Bottom-up: resolve each child's own size/position first
        // (same dependency order as Component.applyLayout).
        children.forEach { it.applyLayout() }

        // 2) Arrange children at their natural (unscrolled) stacked
        // position using our own layout, e.g. ColumnLayout. Note we
        // deliberately do NOT auto-measure/resize *this* container from
        // its content like a normal wrap-content Component would - a
        // ScrollContainer's whole job is to stay a fixed viewport onto
        // content that's taller than it is.
        layout?.arrange(this, layoutBox)

        // 3) Now that children sit at their natural positions we can
        // figure out how tall the content actually is and clamp scroll to it.
        updateMaxScroll()
        scrollY = scrollY.coerceIn(0f, maxScrollY)

        // 4) Bake the scroll offset directly into each child's local Y.
        // Every widget in this codebase draws using *absolute* global
        // coordinates (globalX()/globalY()), not a transform stack, so the
        // offset has to live in the real coordinates - that's what keeps
        // clicks/hover/hit-testing and rendering permanently in sync
        // without needing any custom event-translation or hitTestDeep
        // overrides on this class.
        for (child in children) {
            child.y -= scrollY
        }
    }

    fun scrollBy(delta: Float) {
        scrollY = (scrollY + delta).coerceIn(0f, maxScrollY)
    }

    fun scrollTo(value: Float) {
        scrollY = value.coerceIn(0f, maxScrollY)
    }

    override fun render(ctx: RenderContext) {
        // clipChildren = true (set in init) makes renderChildren() clip to
        // our own bounds automatically - see Component.renderChildren.
        renderChildren(ctx)
        drawScrollbar(ctx)
    }

    private fun drawScrollbar(ctx: RenderContext) {
        if (maxScrollY <= 0f) return // nothing to scroll, no bar needed

        val scope = ctx.scope
        val trackX = globalX() + width - scrollbarWidth
        val trackY = globalY()

        trackColor?.let {
            scope.drawRect(trackX, trackY, scrollbarWidth, height, it)
        }

        val contentHeight = height + maxScrollY
        val thumbHeight = max((height * height / contentHeight), 12f).coerceAtMost(height)
        val scrollRange = height - thumbHeight
        val thumbY = trackY + if (maxScrollY > 0f) (scrollY / maxScrollY) * scrollRange else 0f

        scope.drawRect(trackX, thumbY, scrollbarWidth, thumbHeight, scrollbarColor)
    }

    override fun onEvent(event: InputEvent): Boolean {
        if (event is MouseEvent.Scroll) {
            if (!contains(event.x, event.y)) return false
            if (maxScrollY <= 0f) return false

            scrollBy(-event.vertical.toFloat() * 18f)
            event.consume()
            return true
        }
        return false
    }

    /**
     * Layout-aware scroll bounds calculation. Must be called with children
     * at their *natural* (unscrolled) positions.
     */
    private fun updateMaxScroll() {
        val bottomMost = children.maxOfOrNull { child -> child.y + child.height } ?: 0f
        val contentHeight = bottomMost + layoutBox.padding
        maxScrollY = (contentHeight - height).coerceAtLeast(0f)
    }
}