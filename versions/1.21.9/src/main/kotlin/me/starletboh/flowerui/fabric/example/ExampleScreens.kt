package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.api.FlowerUI
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.widgets.ButtonWidget


object ExampleScreens {

    private val factories: List<() -> FlowerScreen> = listOf(
        { ExampleScreen() },
        { ExampleScrollScreen() },
        { ExampleFormScreen() },

        { ColorPickerScreen()}
    )

    private var index = 0

    
    fun current(): FlowerScreen = factories[index]()

    
    fun next(): FlowerScreen {
        index = (index + 1) % factories.size
        return factories[index]()
    }

    
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
