package me.starletboh.flowerui.ui.registry

import me.starletboh.flowerui.ui.widgets.Widget

object WidgetRegistry {

    private val widgets = mutableMapOf<String, () -> Widget>()

    fun register(id: String, factory: () -> Widget) {
        widgets[id] = factory
    }

    fun create(id: String): Widget? {
        return widgets[id]?.invoke()
    }

    fun all(): Set<String> = widgets.keys
}