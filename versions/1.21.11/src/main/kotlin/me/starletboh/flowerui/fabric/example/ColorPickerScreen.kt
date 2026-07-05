package me.starletboh.flowerui.fabric.example

import me.starletboh.flowerui.theme.ThemeContext
import me.starletboh.flowerui.theme.ThemeRegistry
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.layout.Align
import me.starletboh.flowerui.ui.layout.ColumnLayout
import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.ui.widgets.ColorPickerWidget
import me.starletboh.flowerui.ui.widgets.PanelWidget
import me.starletboh.flowerui.ui.widgets.TextInputWidget
import me.starletboh.flowerui.ui.widgets.TextWidget

/**
 * Demonstrates ColorPickerWidget paired with a live "current value" readout
 * and a typeable hex field that feeds back into the picker - covers both
 * directions: picker -> text, and text -> picker.
 */
class ColorPickerScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    private lateinit var panel: PanelWidget
    private lateinit var picker: ColorPickerWidget
    private lateinit var valueLabel: TextWidget
    private lateinit var hexInput: TextInputWidget

    override fun build(root: RootComponent) {

        // NOTE: fixed size rather than left at 0/0 to auto-measure -
        // TextWidget and ColorPickerWidget currently determine their own
        // width/height inside render() (text measurement, swatch layout)
        // rather than during the measure pass, so an auto-sized parent
        // would be a frame late resolving to the right size on first open.
        panel = PanelWidget().apply {
            width = 200f
            height = 240f
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 10f, padding = 12f, alignX = Align.CENTER)
        }

        panel.add(TextWidget("Color Picker").apply { scale = 1.2f })

        picker = ColorPickerWidget().apply {
            setColor(0xFF4FC3F7.toInt())
            onColorChanged = { updateReadout() }
        }
        panel.add(picker)

        // Live "current value" display - ARGB hex + alpha as a percentage,
        // so both the color and its transparency are visible as text, not
        // just as a swatch.
        valueLabel = TextWidget("").apply { height = 10f }
        panel.add(valueLabel)

        hexInput = TextInputWidget().apply {
            width = picker.squareSize + picker.gap + picker.barThickness + picker.gap + picker.previewSize
            height = 16f
            placeholder = "#AARRGGBB or #RRGGBB"
            onSubmit = { text -> applyHex(text) }
        }
        panel.add(hexInput)

        updateReadout()

        root.add(panel)

        ExampleScreens.addShuffleButton(root)
    }

    private fun updateReadout() {
        val argb = picker.color
        val alphaPct = ((argb ushr 24) and 0xFF) * 100 / 255
        valueLabel.text = String.format("#%08X  (alpha %d%%)", argb, alphaPct)
    }

    /** Parses "#RRGGBB" (assumed fully opaque) or "#AARRGGBB" typed into [hexInput]. */
    private fun applyHex(raw: String) {
        val cleaned = raw.removePrefix("#").trim()
        val value = cleaned.toLongOrNull(16) ?: return

        val argb = when (cleaned.length) {
            6 -> (0xFF shl 24) or (value.toInt() and 0xFFFFFF)
            8 -> value.toInt()
            else -> return
        }

        picker.setColor(argb)
        updateReadout()
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