# 🌸 FlowerUI

**FlowerUI** is a lightweight, themeable UI framework for Fabric mods, architected specifically for Kotlin.

FlowerUI streamlines the development of Minecraft interfaces by replacing boilerplate-heavy manual rendering with a modular, high-performance widget system. It provides a comprehensive suite of automatic layouts, runtime theming, and platform-agnostic abstractions, allowing developers to focus on feature implementation rather than rendering mechanics.

---

## Features

* **Kotlin-First API:** Clean, idiomatic DSL for declarative UI construction.
* **Automatic Layout Engine:** Flexible positioning using `RowLayout`, `ColumnLayout`, `GridLayout`, and `FlexLayout`.
* **Runtime Theming:** Dynamic, swappable visual styles.
* **SVG Rendering Backend:** Scalable graphics with hardware-accelerated texture caching.
* **Comprehensive Widget Library:** Ready-to-use components for complex interfaces.
* **Event Routing:** Built-in handling for mouse, keyboard, and clipboard interactions.
* **Extensible Architecture:** Platform-agnostic core with a clean separation of concerns.

---

## Installation

FlowerUI is distributed via the **JitPack** repository.

### Kotlin DSL

Configure your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

```

Add the dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.starletboh:FlowerUI:master-SNAPSHOT")
}

```

### Groovy

Configure your `settings.gradle`:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}

```

Add the dependency to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.starletboh:FlowerUI:master-SNAPSHOT'
}

```

---

## Requirements

* **Java:** 21
* **Minecraft:** 1.21.x
* **Fabric Loader**
* **Fabric API**
* **Fabric Language Kotlin**

---

## Implementation Example

```kotlin
class ExampleScreen : FlowerScreen() {

    override val themeContext = ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))

    override fun build(root: RootComponent) {
        val panel = PanelWidget().apply {
            width = 220f
            height = 120f
            layout = ColumnLayout()
            layoutBox = LayoutBox(padding = 12f, gap = 8f)
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

Launch the interface via: `FlowerUI.open(ExampleScreen())`

---

## Documentation & Support

For comprehensive guides, API references, and advanced implementation details, consult the [GitHub Wiki](https://github.com/starletboh/FlowerUI/wiki).

---

## License

This project is licensed under the **GNU General Public License v3.0**. See the [LICENSE](https://www.gnu.org/licenses/gpl-3.0.txt) file for further information.

---

## Contributing

Contributions are invited. Please submit pull requests or open issues for feature requests and bug reports.

Would you like me to refine the language further for a specific audience, or are there additional sections you would like to include?
