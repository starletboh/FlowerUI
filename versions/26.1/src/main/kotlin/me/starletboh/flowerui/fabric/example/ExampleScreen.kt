package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.theme.ThemeContext
import me.starletboh.flowerui.theme.ThemeRegistry
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.layout.Align

import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.layout.RowLayout
import me.starletboh.flowerui.ui.widgets.ButtonWidget
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.ui.widgets.PanelWidget

import net.minecraft.resources.Identifier

class ExampleScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    override fun build(root: RootComponent) {

        val panel = PanelWidget().apply {
            width = 260f
            height = 180f
            layout = RowLayout()
            layoutBox = LayoutBox(
                gap = 12f,
                padding = 12f,
                alignX = Align.CENTER,
                alignY=Align.CENTER,
            )
        }

        fun iconButton(icon: Identifier, action: () -> Unit): ButtonWidget {
            return ButtonWidget().apply {
                width = 10f
                height = 10f
                radius = 10
                backgroundColor = 0xFFFFFF00.toInt()
                onClick = action
                iconTexture = icon
                iconSize = 20f
            }
        }

        val modrinth = Identifier.fromNamespaceAndPath("flowerui", "icons/modrinth.png")
        val discord = Identifier.fromNamespaceAndPath("flowerui", "icons/discord.png")
        val github = Identifier.fromNamespaceAndPath("flowerui", "icons/github.png")

        panel.add(iconButton(modrinth) { println("Modrinth") })
        panel.add(iconButton(discord) { println("Discord") })
        panel.add(iconButton(github) { println("GitHub") })

        root.add(panel)

        ExampleScreens.addShuffleButton(root)
    }

    override fun render(ctx: RenderContext) {

        val w = ctx.scope.getScreenWidth()
        val h = ctx.scope.getScreenHeight()

        val panel = root.children.first() as PanelWidget

        panel.x = (w - panel.width) / 2f
        panel.y = (h - panel.height) / 2f

        root.applyLayout()
        root.render(ctx)
        panel.applyLayout()
    }



    override fun rerender() {}
}