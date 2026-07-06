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
