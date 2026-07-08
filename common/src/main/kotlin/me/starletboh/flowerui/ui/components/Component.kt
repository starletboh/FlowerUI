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

    
    abstract fun render(ctx: RenderContext)

    
    fun dispatchEvent(event: InputEvent): Boolean {


        for (child in children.asReversed()) {
            if (child.dispatchEvent(event)) return true
        }


        return onEvent(event)
    }

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