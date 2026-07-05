package me.starletboh.flowerui.font

import me.starletboh.flowerui.ui.render.RenderScope

interface FontHandle {

    fun draw(
        text: String,
        x: Float,
        y: Float,
        color: Int,
        scope: RenderScope,
        shadow: Boolean = false
    )

    fun measure(text: String): Float
}