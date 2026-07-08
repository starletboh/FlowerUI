package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.theme.ThemeContext
import me.starletboh.flowerui.theme.ThemeRegistry
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.layout.Align
import me.starletboh.flowerui.ui.layout.ColumnLayout
import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.layout.RowLayout
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.ui.widgets.ButtonWidget
import me.starletboh.flowerui.ui.widgets.ImageWidget
import me.starletboh.flowerui.ui.widgets.PanelWidget
import me.starletboh.flowerui.ui.widgets.ScrollContainer
import me.starletboh.flowerui.ui.widgets.TextWidget
import net.minecraft.util.Identifier
import net.minecraft.util.Util
import java.net.URI
import javax.swing.text.html.ImageView


class AboutScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    private lateinit var docsPanel: PanelWidget
    private lateinit var linksRow: PanelWidget
    private lateinit var logoImage: ImageWidget
    private val docs = listOf(
        "FlowerUI",
        "",
        "A lightweight, themeable UI framework for Fabric mods.",
        "Build screens without touching Minecraft/Fabric classes",
        "directly in your own code.",
        "",
        "Layouts",
        "RowLayout / ColumnLayout stack widgets on one axis, with",
        "optional wrapping and justify-content style distribution.",
        "GridLayout auto-fits columns and supports per-item align.",
        "",
        "Widgets",
        "Panel, Button, Text, Checkbox, TextInput, ColorPicker,",
        "Image and ScrollContainer cover most screen-building needs.",
        "",
        "Theming",
        "Swap themes at runtime via ThemeRegistry - widgets read",
        "colors from ctx.theme, so screens re-skin automatically.",
        "",
        "Use the button in the top-left to cycle through the demo",
        "screens included with the library."
    )

    override fun build(root: RootComponent) {
        logoImage = ImageWidget().apply {

            texture=Identifier.of("flowerui", "icons/minecraft_title.png")
            width = 200f
            height = 40f
        }
        linksRow = PanelWidget().apply {
            backgroundColor = 0x00000000
            layout = RowLayout()
            layoutBox = LayoutBox(gap = 6f, padding = 4f, alignY = Align.CENTER)
        }

        fun linkButton(icon: Identifier, url: String): ButtonWidget {
            return ButtonWidget().apply {
                width = 18f
                height = 18f
                radius = 9
                iconTexture = icon
                iconSize = 14f
                onClick = { Util.getOperatingSystem().open(URI.create(url)) }
            }
        }

        linksRow.add(linkButton(Identifier.of("flowerui", "icons/modrinth.png"), "https://modrinth.com/mod/flowerui"))
        linksRow.add(linkButton(Identifier.of("flowerui", "icons/github.png"), "https://github.com/starletboh/FlowerUI"))
        linksRow.add(linkButton(Identifier.of("flowerui", "icons/discord.png"), "https://dsc.gg/lunartweaks"))

        docsPanel = PanelWidget().apply {
            width = 400f
            height = 200f
        }

        val scroll = ScrollContainer().apply {
            x = 10f
            y = 10f
            width = docsPanel.width - 20f
            height = docsPanel.height - 20f
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 4f, padding = 6f, alignX = Align.START)
        }

        docs.forEach { line ->
            scroll.add(TextWidget(line).apply {
                width = scroll.width - 20f
                height = 10f
            })
        }

        docsPanel.add(scroll)

        root.add(docsPanel)
        root.add(linksRow)
        root.add(logoImage)

    }

    override fun render(ctx: RenderContext) {

        root.applyLayout()

        val w = ctx.scope.getScreenWidth()
        val h = ctx.scope.getScreenHeight()

        docsPanel.x = (w - docsPanel.width) / 2f
        docsPanel.y = (h - docsPanel.height) / 2f

        linksRow.x = w - linksRow.width - 8f
        linksRow.y = 8f

        logoImage.x = (w - logoImage.width) /2
        logoImage.y = docsPanel.y - 50f - logoImage.height
        root.render(ctx)
    }

    override fun rerender() {}
}