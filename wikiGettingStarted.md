# Getting Started

## Requirements

- Java 21
- A Fabric mod project using Fabric Loader `>=0.19.2`, Fabric API, and **Fabric Language Kotlin** (FlowerUI is written in Kotlin and its public API uses Kotlin lambdas/data classes)
- Minecraft `1.21.11` (the only version currently published under `versions/`)

## 1. Add FlowerUI as a dependency

FlowerUI's version modules build a self-contained mod jar (it shadows its own dependencies — Jackson, svgSalamander — into the jar, so you don't need to declare those yourself).

**Option A — local jar (current recommended path while there's no public maven yet):**

1. Build FlowerUI: `./gradlew :versions:1.21.11:build`
2. Grab the resulting jar from `versions/1.21.11/build/libs/` (the `-dev-shadow` or remapped jar, whichever your setup expects).
3. In your mod's `build.gradle.kts`:

```kotlin
dependencies {
    modImplementation(files("libs/flowerui-1.21.11.jar"))
}
```

**Option B — composite build (if you're developing against FlowerUI source directly):**

```kotlin
// settings.gradle.kts
includeBuild("../FlowerUI")
```

```kotlin
// build.gradle.kts
dependencies {
    modImplementation("me.starletboh:flowerui-1.21.11")
}
```

**Option C — Modrinth/CurseForge maven**, once FlowerUI is published there — check the repo's README for current release status before assuming this is available.

Also declare the dependency in your own `fabric.mod.json` so users get a proper "missing dependency" error instead of a silent crash:

```json
"depends": {
  "flower-ui": "*"
}
```

## 2. Make sure FlowerUI is initialized

FlowerUI's Fabric entrypoint (`me.starletboh.flowerui.fabric.FlowerUIFabric`) registers itself as a client entrypoint and initializes the render backend, built-in widgets, and built-in themes on the first client tick. As long as it's present as a dependency and Fabric Loader loads it, you don't need to call anything yourself before using `FlowerUI.open(...)`.

## 3. Build your first screen

A screen extends `FlowerScreen` and implements three things:

- `themeContext` — which theme this screen uses
- `build(root)` — construct your widget tree once
- `render(ctx)` — runs every frame; reposition anything that depends on screen size, then lay out and draw

```kotlin
import me.starletboh.flowerui.theme.ThemeContext
import me.starletboh.flowerui.theme.ThemeRegistry
import me.starletboh.flowerui.ui.FlowerScreen
import me.starletboh.flowerui.ui.components.RootComponent
import me.starletboh.flowerui.ui.layout.Align
import me.starletboh.flowerui.ui.layout.ColumnLayout
import me.starletboh.flowerui.ui.layout.LayoutBox
import me.starletboh.flowerui.ui.render.RenderContext
import me.starletboh.flowerui.ui.widgets.ButtonWidget
import me.starletboh.flowerui.ui.widgets.PanelWidget
import me.starletboh.flowerui.ui.widgets.TextWidget

class MyScreen : FlowerScreen() {

    override val themeContext = ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    private lateinit var panel: PanelWidget

    override fun build(root: RootComponent) {
        panel = PanelWidget().apply {
            width = 200f
            height = 120f
            layout = ColumnLayout()
            layoutBox = LayoutBox(gap = 8f, padding = 12f, alignX = Align.CENTER)
        }

        panel.add(TextWidget("Hello, FlowerUI").apply { scale = 1.2f })

        panel.add(ButtonWidget().apply {
            text = "Click me"
            width = 100f
            height = 20f
            onClick = { println("clicked!") }
        })

        root.add(panel)
    }

    override fun render(ctx: RenderContext) {
        val w = ctx.scope.getScreenWidth()
        val h = ctx.scope.getScreenHeight()

        // reposition anything that depends on current screen size
        panel.x = (w - panel.width) / 2f
        panel.y = (h - panel.height) / 2f

        root.applyLayout() // resolve sizes + positions for this frame
        root.render(ctx)   // draw
    }

    override fun rerender() {}
}
```

## 4. Open it

From any client-side code (a keybind handler, a command, a button click elsewhere):

```kotlin
FlowerUI.open(MyScreen())
```

`FlowerUI.open` hands the screen to whatever platform adapter registered itself (`FlowerUIPlatform`), which on Fabric wraps it in a `FlowerUIScreen` and calls `MinecraftClient.setScreen(...)`.

## 5. `build()` vs `render()` — what goes where

- **`build(root)` runs once**, the first time the screen opens. This is where you construct widgets, set their static properties (`layout`, `layoutBox`, callbacks), and `root.add(...)` them. Widget instances persist for the screen's lifetime — this is also where you'd store `lateinit var` references you need to mutate later (e.g. to update a label's text in response to a click).
- **`render(ctx)` runs every frame.** Only do two things here: reposition anything that depends on the current screen size (most screens center a root panel this way), then call `root.applyLayout()` followed by `root.render(ctx)`, in that order. Don't rebuild the widget tree here.

## 6. A note on sizing

Layouts do a real measure + arrange pass (see **[Layout System](Layout-System)**): if you leave a container's `width`/`height` at `0`, it will size itself to its children once they're rendered. `TextWidget` and `ColorPickerWidget` currently resolve their own size *inside* `render()` rather than during the measure pass, which means an auto-sized parent containing one of these can be a single frame late resolving to its final size on first open. If that matters for your screen, give the parent an explicit fixed size instead (see `ColorPickerScreen` in the example screens for exactly this workaround).

## Next steps

- **[Widget Reference](Widget-Reference)** for every built-in widget and its options.
- **[Layout System](Layout-System)** for how Row/Column/Grid/Flex layouts behave.
- **[Theming](Theming)** to customize colors or add your own theme.
- Look through `versions/1.21.11/.../fabric/example/` for complete, working screens (icon rows, a scrollable list, a settings form, a link/about screen, and a color picker with a live hex readout) wired together with a shared "Next Screen →" button.
