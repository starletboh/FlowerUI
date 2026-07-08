package me.starletboh.flowerui.fabric.screen

import me.starletboh.flowerui.events.input.KeyEvent
import me.starletboh.flowerui.events.input.MouseEvent
import me.starletboh.flowerui.fabric.render.FabricRenderScope
import me.starletboh.flowerui.input.InputRouter
import me.starletboh.flowerui.theme.ThemeManager
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.render.RenderContext
import net.minecraft.client.gui.Click

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.input.CharInput
import net.minecraft.client.input.KeyInput
import net.minecraft.text.Text

class FlowerUIScreen(
    private val screen: FlowerScreen
) : Screen(Text.literal("FlowerUI")) {
    val scale = client?.window?.scaleFactor?.toDouble()
    override fun init() {
        screen.root.width = width.toFloat()
        screen.root.height = height.toFloat()

        screen.init()
    }

    override fun render(drawContext: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {

        val scope = FabricRenderScope(drawContext)

        val ctx = RenderContext(
            deltaTime = delta,
            time = System.currentTimeMillis(),
            mouseX = mouseX.toDouble(),
            mouseY = mouseY.toDouble(),
            mouseDown = false,
            mouseButton = -1,
            scope = scope,
            theme = screen.themeContext.resolve(),
            scale = scale!!.toFloat()
        )

        screen.root.width = width.toFloat()
        screen.root.height = height.toFloat()

        screen.root.applyLayout()

        screen.render(ctx)
    }
    override fun mouseClicked(click: Click, doubled: Boolean): Boolean {
        val sx = click.x()
        val sy = click.y()

        InputRouter.handle(
            MouseEvent.Click(sx, sy, click.button())
        )
        return super.mouseClicked(click, doubled)
    }

    override fun mouseReleased(click: Click): Boolean {
        val sx = click.x()
        val sy = click.y()
        InputRouter.handle(
            MouseEvent.Release(sx, sy, click.button())
        )
        return super.mouseReleased(click)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {

        InputRouter.handle(
            MouseEvent.Move(mouseX, mouseY)
        )
    }
    override fun charTyped(input: CharInput): Boolean {
        val ch = input.codepoint.toChar()

        InputRouter.handle(
            KeyEvent.CharTyped(ch)
        )

        return super.charTyped(input)
    }
    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double): Boolean {
        InputRouter.handle(MouseEvent.Scroll(mouseX, mouseY, horizontal, vertical))
        return true
    }
    override fun keyPressed(input: KeyInput): Boolean {
        InputRouter.handle(
            KeyEvent.Press(input.key(), input.scancode(), input.modifiers())
        )

        return super.keyPressed(input)
    }

    override fun keyReleased(input: KeyInput): Boolean {
        InputRouter.handle(KeyEvent.Release(input.key(), input.scancode(), input.modifiers()))
        return super.keyReleased(input)
    }
    override fun tick() {
        screen.tick()
    }

    override fun close() {
        screen.close()
        super.close()
    }
}