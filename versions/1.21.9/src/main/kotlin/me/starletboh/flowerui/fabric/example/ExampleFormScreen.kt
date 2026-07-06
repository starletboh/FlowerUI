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
import me.starletboh.flowerui.ui.widgets.*

class ExampleFormScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    private lateinit var panel: PanelWidget
    private lateinit var scroll: ScrollContainer

    override fun build(root: RootComponent) {

        panel = PanelWidget().apply {
            width = 260f
            height = 220f
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 10f, padding = 12f, alignX = Align.START)
        }

        panel.add(TextWidget("Settings").apply {
            scale = 1.2f
        })
        panel.add(TextWidget("This is an example screen").apply {
            scale = 0.5f
        })
        panel.add(TextWidget("and settings wont be saved.").apply {
            scale = 0.5f
        })

        // -------------------------
        // SCROLL AREA (IMPORTANT FIX)
        // -------------------------
        scroll = ScrollContainer().apply {
            width = panel.width - 24f
            height = 170f
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 6f, padding = 6f, alignX = Align.START)
        }

        // -------------------------
        // ROW HELPERS
        // -------------------------
        fun settingRow(label: String, initial: Boolean): PanelWidget {
            val row = PanelWidget().apply {
                width = scroll.width - 12f
                height = 14f
                backgroundColor = 0x00000000
                layout = RowLayout()
                layoutBox = LayoutBox(gap = 6f, alignY = Align.CENTER)
            }

            row.add(CheckboxWidget().apply {
                width = 12f
                height = 12f
                checked = initial
            })

            row.add(TextWidget(label))

            return row
        }

        fun toggleRow(label: String, initial: Boolean): PanelWidget {
            val row = PanelWidget().apply {
                width = scroll.width - 12f
                height = 14f
                backgroundColor = 0x00000000
                layout = RowLayout()
                layoutBox = LayoutBox(gap = 6f, alignY = Align.CENTER)
            }

            row.add(ToggleWidget().apply {
                width = 22f
                height = 12f
                value = initial
            })

            row.add(TextWidget(label))

            return row
        }

        fun sliderRow(label: String, value: Float, min: Float, max: Float): PanelWidget {
            val row = PanelWidget().apply {
                width = scroll.width - 12f
                height = 28f
                backgroundColor = 0x00000000
                layout = ColumnLayout()
                layoutBox = LayoutBox(gap = 4f)
            }

            row.add(TextWidget(label))

            row.add(SliderWidget().apply {
                width = row.width
                height = 10f
                this.value = value
                this.min = min
                this.max = max
            })

            return row
        }

        fun dropdownRow(label: String, items: List<String>, initialIndex: Int = 0): PanelWidget {
            val row = PanelWidget().apply {
                width = scroll.width - 12f
                height = 34f
                backgroundColor = 0x00000000
                layout = ColumnLayout()
                layoutBox = LayoutBox(gap = 4f)
            }

            row.add(TextWidget(label))

            // NOTE: the dropdown's own open item list renders via the Overlay
            // system (see RootComponent/Overlay), so it draws on top of and
            // stays clickable over the ScrollContainer/panel it sits inside,
            // instead of being clipped or unclickable.
            row.add(DropdownWidget<String>().apply {
                width = row.width
                height = 16f
                this.items = items
                selectedIndex = initialIndex
                labelProvider = { it }
                onChange = { println("Selected: $it") }
            })

            return row
        }

        // -------------------------
        // ADD EVERYTHING TO SCROLL
        // -------------------------
        scroll.add(settingRow("Enable animations", true))
        scroll.add(settingRow("Show tooltips", false))
        scroll.add(settingRow("Compact mode", false))
        scroll.add(toggleRow("Example Toggle", false))
        scroll.add(sliderRow("Example Slider", 11f, 1f, 12f))
        scroll.add(dropdownRow("Example Dropdown", listOf("Option A", "Option B", "Option C")))

        // IMPORTANT: scroll goes inside panel
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