# FlowerUI

A clean, dynamic Minecraft UI library for **Fabric 1.21.x** written in Kotlin.

Build custom screens in your own mod **without importing any Minecraft or Fabric classes**.

---

## Project structure

```
FlowerUI/
├── common/                          # Platform-agnostic UI library
│   └── src/main/kotlin/me/sage/flowerui/
│       ├── FlowerUI.kt              ← Global API entry point
│       ├── asset/
│       │   ├── Renderer.kt          ← Text-draw abstraction
│       │   └── TextRef.kt           ← Platform bridge interface
│       ├── svg/
│       │   ├── SvgLoader.kt         ← SVG → BufferedImage rasteriser
│       │   └── source/
│       │       ├── SvgSource.kt
│       │       └── RectSvg.kt
│       ├── ui/
│       │   ├── core/
│       │   │   ├── UiElement.kt     ← Base element interface
│       │   │   ├── UiScreen.kt      ← Base screen class
│       │   │   └── ElementRef.kt    ← Typed mutable element refs
│       │   ├── element/
│       │   │   ├── RectElement.kt
│       │   │   ├── TextElement.kt
│       │   │   ├── ButtonElement.kt
│       │   │   ├── TextInputElement.kt
│       │   │   ├── CheckboxElement.kt
│       │   │   ├── SliderElement.kt
│       │   │   └── SvgRectElement.kt
│       │   ├── layout/
│       │   │   └── Layout.kt        ← Row, Column, Alignment
│       │   ├── render/
│       │   │   └── RenderScope.kt   ← Draw API interface
│       │   └── style/
│       │       └── Colors.kt        ← Shared ARGB constants
│       └── example/
│           └── ExampleScreen.kt     ← Usage demo
└── versions/
    └── 1.21.11/                     # Fabric platform implementation
        └── src/main/kotlin/me/sage/flowerui/fabric/
            ├── FlowerUIMod.kt       ← ModInitializer
            ├── FlowerUIClient.kt    ← ClientModInitializer (bootstraps registry)
            ├── FlowerUIScreen.kt    ← Minecraft Screen bridge
            ├── FabricRenderContext.kt
            ├── FabricRendererWrapper.kt
            ├── FabricSvgRenderer.kt
            └── ref/
                └── TextRefImpl.kt
```

---

## Using FlowerUI in your mod

### 1. Add the dependency

```kotlin
// your mod's build.gradle.kts
modImplementation("me.sage:flower-ui:1.0.0")
```

### 2. Create a screen

```kotlin
import me.sage.flowerui.FlowerUI
import me.sage.flowerui.ui.core.UiScreen
import me.sage.flowerui.ui.core.ref
import me.sage.flowerui.ui.element.*
import me.sage.flowerui.ui.layout.Column

class MyScreen : UiScreen() {

    private val nameInput = ref<TextInputElement>()

    override fun init() {
        add(TextElement("My Mod Settings", x = 20f, y = 10f))

        add(Column(x = 20f, y = 30f, spacing = 8f).also { col ->
            col.add(nameInput.set(TextInputElement(0f, 0f, 140f, 20f, "Player name")))
            col.add(ButtonElement(0f, 0f, 140f, 20f, "Save") {
                println("Name: ${nameInput.get().text}")
            })
        })
    }
}
```

### 3. Open it

```kotlin
// Inside a key-binding tick event (or anywhere client-side):
FlowerUI.openScreen(MyScreen())
```

---

## Elements

| Class               | Description                                    |
|---------------------|------------------------------------------------|
| `RectElement`       | Filled rectangle                               |
| `TextElement`       | Single-line text                               |
| `ButtonElement`     | Clickable button with hover state              |
| `TextInputElement`  | Single-line text field with cursor blink       |
| `CheckboxElement`   | Toggle checkbox with optional label            |
| `SliderElement`     | Horizontal float-range slider                  |
| `SvgRectElement`    | Rectangle rendered from an SVG source          |

## Layouts

| Class    | Description                              |
|----------|------------------------------------------|
| `Row`    | Horizontal layout with optional alignment|
| `Column` | Vertical layout with optional alignment  |

## Refs

`ElementRef<T>` lets you hold a typed reference to an element before `init()` runs:

```kotlin
val myButton = ref<ButtonElement>()
// in init():
add(ButtonElement(...).also { myButton.set(it) })
// later:
myButton.get().enabled = false
```
