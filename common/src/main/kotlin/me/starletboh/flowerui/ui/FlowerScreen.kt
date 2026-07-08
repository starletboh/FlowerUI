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





    init {
        UIInvalidation.register(invalidateCallback)




    }

    
    abstract fun build(root: RootComponent)

    
    abstract fun render(ctx: RenderContext)

    
    abstract fun rerender()

    
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