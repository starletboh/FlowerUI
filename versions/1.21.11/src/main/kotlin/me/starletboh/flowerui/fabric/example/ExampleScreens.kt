package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.api.FlowerUI
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.widgets.ButtonWidget

/**
 * Holds every example screen so a "Next Screen" button can cycle through
 * all of them at runtime, instead of only ever opening [ExampleScreen].
 */
object ExampleScreens {

    private val factories: List<() -> FlowerScreen> = listOf(
        { ExampleScreen() },
        { ExampleScrollScreen() },
        { ExampleFormScreen() },
//        { AboutScreen() },
        { ColorPickerScreen()}
    )

    private var index = 0

    /** Opens whichever example screen is currently selected. */
    fun current(): FlowerScreen = factories[index]()

    /** Advances to the next example screen (wraps around) and returns it. */
    fun next(): FlowerScreen {
        index = (index + 1) % factories.size
        return factories[index]()
    }

    /**
     * Adds a small always-on-top button (top-left corner) to [root] that
     * opens the next example screen when clicked. Call this once from each
     * example screen's build() function.
     */
    fun addShuffleButton(root: RootComponent) {
        val button = ButtonWidget().apply {
            text = "Next Screen \u2192"
            width = 90f
            height = 16f
            x = 8f
            y = 8f
            onClick = { FlowerUI.open(next()) }
        }
        root.add(button)
    }
}
