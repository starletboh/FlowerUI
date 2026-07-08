package me.starletboh.flowerui.ui.widgets

import me.starletboh.flowerui.events.input.InputEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.ui.components.Component
import me.starletboh.flowerui.ui.render.RenderContext
import kotlin.math.max


class ScrollContainer : Component() {

    var scrollY = 0f
        private set
    var maxScrollY = 0f
        private set

    
    var scrollbarWidth: Float = 4f
    var scrollbarColor: Int = 0x66FFFFFF
    var trackColor: Int? = 0x22000000

    init {



        clipChildren = true
    }

    override fun applyLayout() {



        children.forEach { it.applyLayout() }







        layout?.arrange(this, layoutBox)



        updateMaxScroll()
        scrollY = scrollY.coerceIn(0f, maxScrollY)








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


        renderChildren(ctx)
        drawScrollbar(ctx)
    }

    private fun drawScrollbar(ctx: RenderContext) {
        if (maxScrollY <= 0f) return

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

    
    private fun updateMaxScroll() {
        val bottomMost = children.maxOfOrNull { child -> child.y + child.height } ?: 0f
        val contentHeight = bottomMost + layoutBox.padding
        maxScrollY = (contentHeight - height).coerceAtLeast(0f)
    }
}