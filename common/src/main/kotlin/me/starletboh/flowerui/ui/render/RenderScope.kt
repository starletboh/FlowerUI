package me.starletboh.flowerui.ui.render

import me.starletboh.flowerui.font.FontHandle

interface RenderScope {

    
    fun drawTexture(
        texture: Any,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        alpha: Float = 1f
    )

    
    fun drawRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int
    )

    
    fun drawOutline(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        thickness: Float = 1f
    )

    
    fun drawText(
        text: String,
        x: Float,
        y: Float,
        color: Int,

        scale: Float = 1f
    )

    
    fun measureTextHeight(
        text: String,

        scale: Float = 1f
    ): Float
    fun measureTextWidth(
        text: String,
        scale: Float = 1f
    ) : Float





    fun push()
    fun pop()

    fun translate(x: Float, y: Float)
    fun scale(x: Float, y: Float)

    
    fun rotate(degrees: Float)




    fun getScreenWidth(): Float
    fun getScreenHeight(): Float
    
    fun pushClip(x: Float, y: Float, width: Float, height: Float)

    fun popClip()

    
    fun setAlpha(alpha: Float)

    fun getAlpha(): Float
}