package me.starletboh.flowerui.ui.render

import me.starletboh.flowerui.font.FontHandle

interface RenderScope {

    /**
     * Draw textured image (SVG textures, icons, UI images)
     */
    fun drawTexture(
        texture: Any,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        alpha: Float = 1f
    )

    /**
     * Draw solid rectangle
     */
    fun drawRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int
    )

    /**
     * Draw outline rectangle (debug / focus / hover states)
     */
    fun drawOutline(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        thickness: Float = 1f
    )

    /**
     * Text rendering
     */
    fun drawText(
        text: String,
        x: Float,
        y: Float,
        color: Int,
//        font: FontHandle? = null,
        scale: Float = 1f
    )

    /**
     * Measure text width
     */
    fun measureTextHeight(
        text: String,
//        font: FontHandle? = null,
        scale: Float = 1f
    ): Float
    fun measureTextWidth(
        text: String,
        scale: Float = 1f
    ) : Float

    // --------------------------------------------------
    // TRANSFORM STACK
    // --------------------------------------------------

    fun push()
    fun pop()

    fun translate(x: Float, y: Float)
    fun scale(x: Float, y: Float)

    /**
     * Optional but IMPORTANT: rotation support for animations
     * (used later for hover effects, loading spinners, etc.)
     */
    fun rotate(degrees: Float)

    // --------------------------------------------------
    // ADVANCED (needed for real UI systems)
    // --------------------------------------------------
    fun getScreenWidth(): Float
    fun getScreenHeight(): Float
    /**
     * Clip rendering region (used for scroll views, panels)
     */
    fun pushClip(x: Float, y: Float, width: Float, height: Float)

    fun popClip()

    /**
     * Global alpha multiplier (propagates through children)
     */
    fun setAlpha(alpha: Float)

    fun getAlpha(): Float
}