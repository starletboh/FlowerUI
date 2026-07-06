# 🌸 FlowerUI

A lightweight, themeable UI framework for Fabric mods written in Kotlin.

FlowerUI makes creating Minecraft interfaces simple by replacing manual `Screen`
rendering with a flexible widget system, automatic layouts, theming, and
platform abstractions.

Whether you're creating a settings menu, HUD editor, configuration screen, or
in-game dashboard, FlowerUI provides reusable widgets and layouts so you can
focus on your mod—not rendering code.

---

## Features

- 🌸 Kotlin-first API
- 📐 Automatic layout engine
    - RowLayout
    - ColumnLayout
    - GridLayout
    - FlexLayout
- 🎨 Runtime theme system
- 🖼 SVG rendering backend
- ⚡ Texture caching
- 🧩 Built-in widgets
- 🖱 Mouse & keyboard input
- ⌨ Text input with clipboard support
- 📜 Scroll containers
- 🎭 Widget registry
- 🔌 Platform abstraction
- 🧱 Extensible architecture

---

## Built-in Widgets

- Button
- Panel
- Text
- Image
- Checkbox
- Toggle
- Slider
- Dropdown
- Text Input
- Scroll Container
- Color Picker

---

## Installation

FlowerUI is distributed through **JitPack**.

### Kotlin DSL

```kotlin
repositories {
    maven("[https://jitpack.io](https://jitpack.io)")
}

dependencies {
    // Replace 'master-SNAPSHOT' with a specific branch or commit hash if preferred
    implementation("com.github.starletboh:FlowerUI:master-SNAPSHOT")
}

```

### Groovy

```groovy
repositories {
    maven { url '[https://jitpack.io](https://jitpack.io)' }
}

dependencies {
    // Replace 'master-SNAPSHOT' with a specific branch or commit hash if preferred
    implementation 'com.github.starletboh:FlowerUI:master-SNAPSHOT'
}

```

---

## Requirements

* Java 21
* Minecraft 1.21.x
* Fabric Loader
* Fabric API
* Fabric Language Kotlin

---

## Quick Example

```kotlin
class ExampleScreen : FlowerScreen() {

    override val themeContext =
        ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    override fun build(root: RootComponent) {

        val panel = PanelWidget().apply {
            width = 220f
            height = 120f

            layout = ColumnLayout()
            layoutBox = LayoutBox(
                padding = 12f,
                gap = 8f
            )
        }

        panel.add(TextWidget("Hello FlowerUI"))

        panel.add(ButtonWidget().apply {
            width = 100f
            height = 20f
            text = "Click me"
        })

        root.add(panel)
    }

    override fun render(ctx: RenderContext) {
        root.applyLayout()
        root.render(ctx)
    }
}

```

Open it anywhere using

```kotlin
FlowerUI.open(ExampleScreen())

```

---

## Documentation

The complete documentation is available in the GitHub Wiki.

### Getting Started

Installation, first screen and project setup.

### Layouts

* ColumnLayout
* RowLayout
* GridLayout
* FlexLayout
* LayoutBox

### Widgets

Documentation for every built-in widget.

### Themes

Creating and using custom themes.

### Rendering

How FlowerUI renders widgets and SVG textures.

### Input

Keyboard, mouse, focus and routing.

### Advanced

Creating custom widgets, platform services and extending FlowerUI.

---

## Why FlowerUI?

Minecraft's vanilla GUI system requires manually positioning every element,
handling input yourself and writing rendering code for each widget.

FlowerUI instead lets you describe your interface as a tree of reusable
components while automatically handling:

* measuring
* positioning
* clipping
* rendering
* event routing
* theming

This keeps screens cleaner, easier to maintain and significantly easier to
extend.

---

## Project Structure

```
common/
    Core framework

fabric/
    Fabric implementation

graphics/
    Rendering backends

theme/
    Theme system

ui/
    Widgets
    Layouts
    Components

```

---

## License

FlowerUI is licensed under the GNU General Public License v3.0.

See the LICENSE file for details.

---

## Contributing

Contributions are welcome.

If you discover a bug or have an idea for a new widget or layout, feel free to
open an Issue or Pull Request.
