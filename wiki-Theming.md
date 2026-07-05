# Theming

Every widget reads its colors from `ctx.theme.colors` at render time — nothing hardcodes a color, so switching the active theme re-skins every screen automatically.

## `ThemeDefinition`

```kotlin
data class ThemeDefinition(
    val id: String,
    val name: String,
    val colors: ThemeColors,
    val typography: ThemeTypography,
    val effects: ThemeEffects
)
```

```kotlin
data class ThemeColors(
    val background: Int,
    val surface: Int,
    val primary: Int,
    val secondary: Int,
    val textPrimary: Int,
    val textSecondary: Int,
    val border: Int,
    val hover: Int,
    val pressed: Int,
    val disabled: Int
)
```

All colors are ARGB ints (`0xAARRGGBB`).

## Built-in themes

Registered by `BuiltInThemes.registerAll()` (called once during Fabric init):

| id | Preset |
|---|---|
| `catppuccin_mocha` | `CatppuccinMocha` |
| `flower_dark` | `flowerDark` |
| `flower_light` | `flowerLight` |

`flower_dark` is also the hardcoded fallback in `ThemeRegistry.getOrFallback(...)` if a requested theme id isn't registered.

## Picking a theme for a screen

Every `FlowerScreen` declares its `themeContext`:

```kotlin
override val themeContext = ThemeContext(ThemeRegistry.getOrFallback("catppuccin_mocha"))
```

`ThemeContext` also supports a temporary `override`, resolved via `resolve()` (`override ?: base`) — useful for previewing a theme without committing to it.

## Registering your own theme

```kotlin
val myTheme = ThemeDefinition(
    id = "my_theme",
    name = "My Theme",
    colors = ThemeColors(
        background = 0xFF1E1E1EU.toInt(),
        surface = 0xFF2A2A2EU.toInt(),
        primary = 0xFF7C9CFFU.toInt(),
        secondary = 0xFF9C7CFFU.toInt(),
        textPrimary = 0xFFEAEAEAU.toInt(),
        textSecondary = 0xFFA0A0A0U.toInt(),
        border = 0xFF3A3A3EU.toInt(),
        hover = 0xFF34343AU.toInt(),
        pressed = 0xFF1A1A1EU.toInt(),
        disabled = 0xFF555555U.toInt()
    ),
    typography = /* your ThemeTypography */,
    effects = /* your ThemeEffects */
)

ThemeRegistry.register(myTheme)
```

Register it during your own mod's client init (after FlowerUI's own init has run, so `ThemeRegistry` exists) — then reference it by id the same way as a built-in theme:

```kotlin
override val themeContext = ThemeContext(ThemeRegistry.getOrFallback("my_theme"))
```

## Config-driven theme selection

`ThemeConfig` + `ConfigManager` persist the user's chosen theme id to `config/flowerui.json`, and `ConfigPatchManager` runs registered patches (e.g. `ThemeNormalizationPatch`) on load — this is how a saved config from an older FlowerUI version gets migrated forward instead of breaking when the theme schema changes. If you're persisting your own settings alongside a theme choice, follow the same patch-based migration pattern rather than reading raw JSON directly.
