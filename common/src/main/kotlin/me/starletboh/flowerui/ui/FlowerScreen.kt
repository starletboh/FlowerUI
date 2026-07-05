package me.starletboh.flowerui.ui

import me.starletboh.flowerui.events.UIInvalidation
import me.starletboh.flowerui.input.InputRouter
import me.starletboh.flowerui.theme.ThemeContext

import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.render.RenderContext

abstract class FlowerScreen {

    val root = RootComponent()
    abstract val themeContext: ThemeContext
    private var built = false

    private val invalidateCallback = {
        rerender()
    }

//    private val themeListener: (ThemeDefinition) -> Unit = {
//        rerender()
//    }

    init {
        UIInvalidation.register(invalidateCallback)



//        ThemeManager.addListener(themeListener)
    }

    /**
     * Build UI tree here
     */
    abstract fun build(root: RootComponent)

    /**
     * Render screen
     */
    abstract fun render(ctx: RenderContext)

    /**
     * Request redraw from host (Fabric screen)
     */
    abstract fun rerender()

    /**
     * Called once before first render
     */
    open fun init() {
        if (!built) {
            build(root)
            InputRouter.register(root)
            built = true
        }
    }

    open fun tick() {
        root.tick()
    }

    open fun close() {
        UIInvalidation.unregister(invalidateCallback)

        InputRouter.unregister(root)
    }
}