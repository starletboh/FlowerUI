package me.starletboh.flowerui.ui.components

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.ui.layout.Layout
import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.render.RenderContext

abstract class Component {

    var parent: Component? = null
        private set

    val children = mutableListOf<Component>()
    var layout: Layout? = null
    var layoutBox = LayoutBox()
    var x: Float = 0f
    var y: Float = 0f
    var width: Float = 0f
    var height: Float = 0f

    /**
     * When true, children are clipped to this component's bounds before
     * being rendered - so content from a Row/Column/Grid layout that
     * overflows the parent's box (e.g. too many buttons in a fixed-width
     * panel) gets visually cut off at the edge instead of spilling out
     * over whatever is behind it.
     */
    var clipChildren: Boolean = false

    fun add(child: Component) {
        child.parent = this
        children += child
    }

    fun remove(child: Component) {
        child.parent = null
        children -= child
    }

    fun globalX(): Float = parent?.globalX()?.plus(x) ?: x
    fun globalY(): Float = parent?.globalY()?.plus(y) ?: y

    /** Walks up the parent chain to find the RootComponent, if this component is attached to one. */
    fun findRoot(): RootComponent? {
        var c: Component? = this
        while (c != null) {
            if (c is RootComponent) return c
            c = c.parent
        }
        return null
    }

    open fun contains(px: Double, py: Double): Boolean {
        val gx = globalX().toDouble()
        val gy = globalY().toDouble()

        return px >= gx && px <= gx + width &&
                py >= gy && py <= gy + height
    }

    /**
     * NEW: context-based rendering (required for framework design)
     */
    abstract fun render(ctx: RenderContext)

    /**
     * Recursive helper for UI tree rendering
     */
    fun dispatchEvent(event: InputEvent): Boolean {

        // children FIRST (top-down hit priority)
        for (child in children.asReversed()) {
            if (child.dispatchEvent(event)) return true
        }

        // only if no child consumed it
        return onEvent(event)
    }
    // Inside Component.kt
    open fun renderChildren(ctx: RenderContext) {
        if (clipChildren) {
            ctx.scope.pushClip(globalX(), globalY(), width, height)
        }
        for (child in children) {
            child.render(ctx)
        }
        if (clipChildren) {
            ctx.scope.popClip()
        }
    }
    open fun hitTestDeep(x: Double, y: Double): Component? {

        if (!contains(x, y)) return null

        for (child in children.asReversed()) {
            child.hitTestDeep(x, y)?.let { return it }
        }

        return this
    }
    open fun onEvent(event: InputEvent): Boolean {
        return false
    }
    open fun applyLayout() {

        // BOTTOM-UP: resolve every child's own size/position first. This
        // matters because a container that wraps its content (width/height
        // left at 0, meaning "size me to my children") can only measure
        // itself correctly once its children's *final* sizes are known -
        // which for a child that is itself a container means recursing all
        // the way down first.
        children.forEach { it.applyLayout() }

        val l = layout ?: return

        if (width <= 0f || height <= 0f) {
            val availableWidth = if (width > 0f) width else (parent?.width?.minus(layoutBox.padding * 2) ?: Float.MAX_VALUE)
            val availableHeight = if (height > 0f) height else (parent?.height?.minus(layoutBox.padding * 2) ?: Float.MAX_VALUE)

            val measured = l.measure(this, layoutBox, availableWidth, availableHeight)

            if (width <= 0f) width = measured.width
            if (height <= 0f) height = measured.height
        }

        l.arrange(this, layoutBox)
    }
    open fun tick() {}
}