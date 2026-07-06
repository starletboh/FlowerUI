package me.starletboh.flowerui.fabric.render

import me.starletboh.flowerui.font.FontHandle
import me.starletboh.flowerui.ui.render.RenderScope
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import org.joml.Matrix3x2fStack

class FabricRenderScope(
    private val ctx: DrawContext
) : RenderScope {
    // always synced from Minecraft context
    private val matrix: Matrix3x2fStack
        get() = ctx.matrices

    override fun push() { ctx.matrices.pushMatrix()}

    override fun pop() { ctx.matrices.popMatrix()}
    override fun getScreenWidth(): Float =
        MinecraftClient.getInstance().window.scaledWidth.toFloat()

    override fun getScreenHeight(): Float =
        MinecraftClient.getInstance().window.scaledHeight.toFloat()
    override fun translate(x: Float, y: Float) {
        ctx.matrices.translate(x, y)
    }

    override fun scale(x: Float, y: Float) {
        ctx.matrices.scale(x, y)
    }

    override fun rotate(degrees: Float) {
        ctx.matrices.rotate(degrees)
    }

    override fun pushClip(x: Float, y: Float, width: Float, height: Float) {
        // DrawContext.enableScissor pushes onto its own internal
        // ScissorStack, which already intersects with whatever scissor is
        // currently active (see ScissorStack.push in DrawContext) - so we
        // don't need to track or intersect rects ourselves here at all.
        // Every render state element (text, quads, textures, ...) also
        // captures scissorStack.peekLast() at the moment it's submitted,
        // baked into the element itself - so unlike a typical immediate-
        // mode scissor, popping/pushing later can't retroactively affect
        // something already submitted. No manual flush is needed either.
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

        ctx.drawTexture(
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

        ctx.fill(xi, yi, xf, yi + t, color)          // top
        ctx.fill(xi, yf - t, xf, yf, color)          // bottom
        ctx.fill(xi, yi, xi + t, yf, color)          // left
        ctx.fill(xf - t, yi, xf, yf, color)          // right
    }

    override fun drawText(
        text: String,
        x: Float,
        y: Float,
        color: Int,
        scale: Float
    ) {
        ctx.drawText(
            MinecraftClient.getInstance().textRenderer,
            text,
            x.toInt(),
            y.toInt(),
            color,
            false
        )
    }

    override fun measureTextHeight(text: String, scale: Float): Float {
        return MinecraftClient.getInstance().textRenderer.fontHeight * scale
    }

    override fun measureTextWidth(text: String, scale: Float): Float {
        return MinecraftClient.getInstance().textRenderer.getWidth(text).toFloat()
    }


}