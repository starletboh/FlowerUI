package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.theme.ThemeContext
import me.starletboh.flowerui.theme.ThemeRegistry
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.layout.Align
import me.starletboh.flowerui.ui.layout.ColumnLayout
import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.ui.widgets.PanelWidget
import me.starletboh.flowerui.ui.widgets.ScrollContainer
import me.starletboh.flowerui.ui.widgets.TextWidget


class ExampleScrollScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    private lateinit var panel: PanelWidget

    override fun build(root: RootComponent) {

        panel = PanelWidget().apply {
            width = 220f
            height = 200f
        }

        val scroll = ScrollContainer().apply {
            width = panel.width
            height = panel.height
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 6f, padding = 8f, alignX = Align.START)
        }

        repeat(25) { i ->
            scroll.add(TextWidget("Row ${i + 1}").apply {
                width = scroll.width - 16f
                height = 12f
            })
        }

        panel.add(scroll)
        root.add(panel)

        ExampleScreens.addShuffleButton(root)
    }

    override fun render(ctx: RenderContext) {

        val w = ctx.scope.getScreenWidth()
        val h = ctx.scope.getScreenHeight()

        panel.x = (w - panel.width) / 2f
        panel.y = (h - panel.height) / 2f

        root.applyLayout()
        root.render(ctx)
    }

    override fun rerender() {}
}
