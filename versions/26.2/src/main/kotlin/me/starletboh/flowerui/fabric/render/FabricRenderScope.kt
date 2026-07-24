package me.starletboh.flowerui.fabric.render

import me.starletboh.flowerui.font.FontHandle
import me.starletboh.flowerui.ui.render.RenderScope
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier


import org.joml.Matrix3x2fStack
import java.awt.Font

class FabricRenderScope(
    private val ctx: GuiGraphicsExtractor
) : RenderScope {

    private val matrix: Matrix3x2fStack
        get() = ctx.pose()
    private val mc = Minecraft.getInstance()
    override fun push() { matrix.pushMatrix()}

    override fun pop() { matrix.popMatrix()}
    override fun getScreenWidth(): Float =
        mc.window.guiScaledWidth.toFloat()

    override fun getScreenHeight(): Float =
        mc.window.guiScaledHeight.toFloat()
    override fun translate(x: Float, y: Float) {
        matrix.translate(x, y)
    }

    override fun scale(x: Float, y: Float) {
        matrix.scale(x, y)
    }

    override fun rotate(degrees: Float) {
        matrix.rotate(degrees)
    }

    override fun pushClip(x: Float, y: Float, width: Float, height: Float) {


        ctx.enableScissor(
            x.toInt(),
            y.toInt(),
            (x + width).toInt(),
            (y + height).toInt()
        )
    }

    override fun popClip() {
        ctx.disableScissor()
    }

    override fun setAlpha(alpha: Float) {

    }

    override fun getAlpha(): Float {
        TODO("Not yet implemented")
    }

    override fun drawTexture(
        texture: Any,
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        alpha: Float
    ) {
        val id = texture as Identifier

        ctx.blit(
            RenderPipelines.GUI_TEXTURED,
            id,
            x.toInt(),
            y.toInt(),
            0f,
            0f,
            width.toInt(),
            height.toInt(),
            width.toInt(),
            height.toInt()
        )
    }

    override fun drawRect(x: Float, y: Float, width: Float, height: Float, color: Int) {
        ctx.fill(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt(), color)
    }

    override fun drawOutline(x: Float, y: Float, width: Float, height: Float, color: Int, thickness: Float) {
        val t = thickness.toInt().coerceAtLeast(1)
        val xi = x.toInt()
        val yi = y.toInt()
        val xf = (x + width).toInt()
        val yf = (y + height).toInt()

        ctx.fill(xi, yi, xf, yi + t, color)
        ctx.fill(xi, yf - t, xf, yf, color)
        ctx.fill(xi, yi, xi + t, yf, color)
        ctx.fill(xf - t, yi, xf, yf, color)
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        color: Int,
        scale: Float
    ) {
        ctx.text(
            mc.font,
            text,
            x.toInt(),
            y.toInt(),
            color,
            false
        )
    }

    override fun measureTextHeight(text: String, scale: Float): Float {
        return mc.font.lineHeight * scale
    }

    override fun measureTextWidth(text: String, scale: Float): Float {
        return mc.font.width(text).toFloat()
    }


}