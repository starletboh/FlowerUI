# Widget Reference

All widgets extend `Widget` (which extends `Component`). Common properties on every widget/component: `x`, `y`, `width`, `height`, `layout`, `layoutBox`, `clipChildren`. Add children with `parent.add(child)`.

---

### `PanelWidget`

A background panel — the workhorse container most screens build inside.

| Property | Default | Notes |
|---|---|---|
| `backgroundColor: Int?` | `null` (theme surface color) | |
| `radius: Int` | `8` | corner radius |

Clips its children by default (`clipChildren = true` set in `init`) — set to `false` per-instance to opt out.

---

### `ButtonWidget`

| Property | Default | Notes |
|---|---|---|
| `text: String` | `""` | |
| `radius: Int` | `6` | |
| `backgroundColor / hoverColor / pressedColor / textColor: Int?` | `null` | fall back to the theme's button style |
| `iconTexture: Any?` | `null` | platform texture handle (e.g. `Identifier` on Fabric); shown alongside/instead of `text` |
| `iconSize: Float` | `16f` | |
| `onClick: (() -> Unit)?` | `null` | |

---

### `TextWidget(text: String = "")`

| Property | Default | Notes |
|---|---|---|
| `text: String` | constructor arg | |
| `color: Int?` | `null` (theme text color) | |
| `scale: Float` | `1f` | |
| `align: Align` | `START` | horizontal align within its own `width` |

Auto-sizes to the measured text when `width`/`height` are left at `0` — but this happens inside `render()`, not the layout measure pass (see the sizing caveat in [Layout System](Layout-System)).

---

### `CheckboxWidget`

| Property | Default | Notes |
|---|---|---|
| `checked: Boolean` | `false` | |
| `radius: Int` | `4` | |
| `boxColor / checkedColor / borderColor: Int?` | `null` | theme fallback |
| `onCheckedChanged: ((Boolean) -> Unit)?` | `null` | |

---

### `ToggleWidget`

An on/off switch (rounded-pill track + sliding circular knob, generated as cached SVG textures — same approach as `PanelWidget`), as an alternative to `CheckboxWidget`.

| Property | Default | Notes |
|---|---|---|
| `value: Boolean` | `false` | |
| `knobRadius: Float` | `6f` | also determines the knob's diameter |
| `trackOnColor / trackOffColor / knobColor: Int?` | `null` | theme fallback (`primary` / `border` / white) |
| `onChange: ((Boolean) -> Unit)?` | `null` | |

---

### `SliderWidget`

A draggable numeric slider, rendered as a rounded-pill track/fill (cached SVG textures) with a circular knob.

| Property | Default | Notes |
|---|---|---|
| `min / max: Float` | `0f` / `1f` | |
| `value: Float` | `0f` | clamped to `[min, max]` |
| `trackColor / fillColor / knobColor: Int?` | `null` | theme fallback (`border` / `primary` / white) |
| `knobRadius: Float` | `6f` | also determines the knob's diameter |
| `onChange: ((Float) -> Unit)?` | `null` | |

---

### `DropdownWidget<T>`

A generic select dropdown, styled with the same cached SVG rounded-rect look as `PanelWidget`.

| Property | Default | Notes |
|---|---|---|
| `items: List<T>` | `emptyList()` | |
| `selectedIndex: Int` | `-1` | |
| `selectedItem: T?` | derived | read-only |
| `labelProvider: (T) -> String` | `{ it.toString() }` | how each item renders as text |
| `isOpen: Boolean` | `false` (private set) | read-only — opened/closed via click, not settable directly |
| `radius: Int` | `6` | |
| `backgroundColor / hoveredRowColor: Int?` | `null` | theme fallback |
| `onChange: ((T?) -> Unit)?` | `null` | |

The open item list renders through the **[Overlay](Overlays)** system rather than as a normal child — it needs to draw on top of (and stay clickable over) whatever it's placed inside, including a clipped `PanelWidget`. It auto-closes when focus moves elsewhere (clicking away), via `InputRouter.currentFocus`.

---

### `TextInputWidget`

A single-line editable text field with caret, text selection, and clipboard copy/paste.

| Property | Default | Notes |
|---|---|---|
| `text: String` | `""` | |
| `placeholder: String` | `""` | shown when empty and unfocused |
| `maxLength: Int` | `256` | |
| `radius: Int` | `6` | |
| `backgroundColor / focusedBorderColor / textColor / placeholderColor: Int?` | `null` | theme fallback |
| `onChange: ((String) -> Unit)?` | `null` | fires on every edit |
| `onSubmit: ((String) -> Unit)?` | `null` | fires on Enter |

Focus comes from `InputRouter.currentFocus` — click to focus, click elsewhere (or another widget) to blur.

---

### `ColorPickerWidget(palette: List<Int> = defaultPalette)`

A full HSV + alpha color picker: saturation/value square, vertical hue strip, horizontal alpha slider (checkerboard backdrop so transparency is visible), a preview swatch, and a quick-pick palette row.

| Property | Default | Notes |
|---|---|---|
| `color: Int` | derived | **read-only**, current selection as `0xAARRGGBB` |
| `onColorChanged: ((Int) -> Unit)?` | `null` | fires on every drag/click that changes the color |
| `squareSize / barThickness / previewSize / gap / swatchSize / swatchGap: Float` | various | control layout; the widget self-sizes from these |

Call `setColor(argb: Int)` to set the picker's state programmatically (e.g. from a parsed hex string) — this does **not** fire `onColorChanged`.

This widget determines its own size inside `render()`; see the sizing caveat in [Layout System](Layout-System) if you put it inside an auto-sized parent.

---

### `ImageWidget(texture: Any? = null)`

Draws a texture directly — no generated/cached texture involved, the cheapest widget to render. Good for icons, logos, screenshots.

| Property | Default | Notes |
|---|---|---|
| `texture: Any?` | constructor arg | platform texture handle |
| `alpha: Float` | `1f` | |

---

### `ScrollContainer`

A `Component` (not a `Widget`) that vertically scrolls its children. **Always give it an explicit fixed `width`/`height`** — it deliberately does not auto-size to content.

| Property | Default | Notes |
|---|---|---|
| `scrollY: Float` | `0f` (private set) | current scroll offset |
| `maxScrollY: Float` | `0f` (private set) | computed from content height |
| `scrollbarWidth: Float` | `4f` | |
| `scrollbarColor: Int` | `0x66FFFFFF` | |
| `trackColor: Int?` | `0x22000000` | set `null` to hide the track |

`scrollBy(delta: Float)` / `scrollTo(value: Float)` to control scroll position programmatically. Set `layout` (typically `ColumnLayout`) + `layoutBox` to stack children; mouse-wheel scrolling is wired in automatically.

(If you need content to escape a `ScrollContainer`/panel's bounds entirely rather than being clipped by it — e.g. a dropdown opened from inside one — see **[Overlays](Overlays)**.)

---

## Registering a widget with `WidgetRegistry`

Built-in widgets are pre-registered by the Fabric entrypoint (`registerBuiltinWidgets()` in `FlowerUIFabricImpl`). If you're adding your own reusable widget and want it available by string key (e.g. for a data-driven/config-built screen), register it the same way:

```kotlin
WidgetRegistry.register("my_widget") { MyWidget() }
```

Note `WidgetRegistry` is typed to `Widget`, so a `Component`-only class like `ScrollContainer` can't go through it directly.
