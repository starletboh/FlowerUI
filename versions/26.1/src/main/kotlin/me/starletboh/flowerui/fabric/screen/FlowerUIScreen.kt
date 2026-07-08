package me.starletboh.flowerui.fabric.screen

import me.starletboh.flowerui.events.input.KeyEvent as FlowerKeyEvent
import me.starletboh.flowerui.events.input.MouseEvent as FlowerMouseEvent
import me.starletboh.flowerui.fabric.render.FabricRenderScope
import me.starletboh.flowerui.input.InputRouter
import me.starletboh.flowerui.theme.ThemeManager
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.render.RenderContext
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.input.KeyEvent
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component

class FlowerUIScreen(
    private val screen: FlowerScreen
) : Screen(Component.literal("FlowerUI")) {


    val scale: Float
        get() = minecraft.window.guiScale.toFloat()


    override fun init() {
        screen.root.width = width.toFloat()
        screen.root.height = height.toFloat()
        screen.init()
    }


    override fun extractRenderState(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, delta: Float) {
        val scope = FabricRenderScope(graphics)

        val ctx = RenderContext(
            deltaTime = delta,
            time = System.currentTimeMillis(),
            mouseX = mouseX.toDouble(),
            mouseY = mouseY.toDouble(),
            mouseDown = false,
            mouseButton = -1,
            scope = scope,
            theme = screen.themeContext.resolve(),
            scale = scale
        )

        screen.root.width = width.toFloat()
        screen.root.height = height.toFloat()
        screen.root.applyLayout()

        screen.render(ctx)


        super.extractRenderState(graphics, mouseX, mouseY, delta)
    }


    override fun keyPressed(event: KeyEvent): Boolean {
        InputRouter.handle(
            FlowerKeyEvent.Press(event.key(), event.scancode(), event.modifiers())
        )
        return super.keyPressed(event)
    }

    override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
        InputRouter.handle(
            FlowerMouseEvent.Click(event.x(), event.y(), event.button())
        )
        return super.mouseClicked(event, doubleClick)
    }

    override fun mouseReleased(event: MouseButtonEvent): Boolean {
        InputRouter.handle(
            FlowerMouseEvent.Release(event.x(), event.y(), event.button())
        )
        return super.mouseReleased(event)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        InputRouter.handle(
            FlowerMouseEvent.Move(mouseX, mouseY)
        )
        super.mouseMoved(mouseX, mouseY)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, horizontal: Double, vertical: Double): Boolean {
        InputRouter.handle(FlowerMouseEvent.Scroll(mouseX, mouseY, horizontal, vertical))
        return true
    }

    override fun tick() {
        screen.tick()
    }


    override fun onClose() {
        screen.close()
        super.onClose()
    }
}