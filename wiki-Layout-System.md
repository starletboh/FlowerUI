# Layout System

Every container (`Component`) can have a `layout: Layout?` and a `layoutBox: LayoutBox`. A `Layout` does two distinct things:

```kotlin
interface Layout {
    fun measure(parent: Component, box: LayoutBox, availableWidth: Float, availableHeight: Float): Size
    fun arrange(parent: Component, box: LayoutBox)
}
```

- **`measure`** — "how big does `parent` need to be to fit its children?" Only used when `parent` doesn't already have an explicit `width`/`height` set (i.e. you left it at `0`, meaning "size me to my content").
- **`arrange`** — "given parent's *final* width/height, where does each child go?" This sets `child.x`/`child.y` (and, for stretch behavior, `child.width`/`child.height`).

## How sizing resolves — bottom-up

`Component.applyLayout()` recurses into children *first*, then measures/arranges itself:

```kotlin
open fun applyLayout() {
    children.forEach { it.applyLayout() }   // children resolve their own size first

    val l = layout ?: return
    if (width <= 0f || height <= 0f) {
        val measured = l.measure(this, layoutBox, availableWidth, availableHeight)
        if (width <= 0f) width = measured.width
        if (height <= 0f) height = measured.height
    }
    l.arrange(this, layoutBox)
}
```

This means:

- A container with an **explicit `width`/`height`** behaves like a fixed viewport — its layout only positions children within that fixed size.
- A container **left at `0`/`0`** sizes itself to fit its children automatically (wrap-content), as long as those children already know their own size by the time this runs.

Call `root.applyLayout()` once per frame, before `root.render(ctx)`.

> **Caveat:** a small number of widgets (`TextWidget`, `ColorPickerWidget`) currently determine their own size *inside* `render()` (e.g. measuring text), not during the measure pass. An auto-sized parent containing one of these can be one frame late resolving to its correct size the first time a screen opens. Give the parent a fixed size if that matters for your screen.

## `LayoutBox`

Shared configuration passed to every layout:

```kotlin
data class LayoutBox(
    val startX: Float = 0f,
    val startY: Float = 0f,
    val alignX: Align = Align.START,   // cross-axis align for ColumnLayout / RowLayout wrap
    val alignY: Align = Align.START,   // cross-axis align for RowLayout
    val gap: Float = 4f,
    val padding: Float = 0f
)
```

`Align` is `START | CENTER | END`.

## `RowLayout`

Lays children left-to-right.

```kotlin
RowLayout(
    wrap: Boolean = false,
    justify: JustifyContent = JustifyContent.START
)
```

- `wrap = true` — children that would overflow the available width flow onto a new line instead of overflowing it.
- `justify` — main-axis (horizontal) distribution per line: `START | CENTER | END | SPACE_BETWEEN | SPACE_AROUND`.
- Cross-axis (vertical) alignment within a line comes from `LayoutBox.alignY`.

## `ColumnLayout`

Lays children top-to-bottom.

```kotlin
ColumnLayout(justify: JustifyContent = JustifyContent.START)
```

- `justify` — main-axis (vertical) distribution: same options as `RowLayout`.
- Cross-axis (horizontal) alignment comes from `LayoutBox.alignX`.

## `GridLayout`

Arranges children into a grid.

```kotlin
GridLayout(
    columns: Int = 0,                        // 0 = auto-fit based on available width
    alignItems: AlignItems = AlignItems.START, // vertical align within a row
    justifyItems: Align = Align.START,         // horizontal align within a column
    stretch: Boolean = false                   // fill column width (+ row height with alignItems = STRETCH)
)
```

`columns = 0` fits as many columns as will fit the available width, based on the first child's width.

## `FlexLayout`

A single-axis flex layout (delegates to `RowLayout`/`ColumnLayout` under the hood):

```kotlin
FlexLayout(
    direction: FlexDirection,                   // ROW | COLUMN
    justifyContent: JustifyContent = JustifyContent.START,
    alignItems: AlignItems = AlignItems.START,  // STRETCH fills the cross-axis size
    wrap: Boolean = false                       // ROW only
)
```

## `ScrollContainer`

Not a `Layout` itself, but worth calling out here: `ScrollContainer` deliberately **does not** auto-size to its content (that would defeat scrolling) — always give it an explicit fixed `width`/`height`. Set its own `layout` (typically `ColumnLayout`) to stack children naturally; `ScrollContainer` arranges them at their natural position, computes `maxScrollY` from the tallest child, then bakes the current scroll offset directly into each child's real `y` (rather than using a transform), which is what keeps rendering, clipping, and click/hover hit-testing all in sync. Mouse wheel scrolling is wired in automatically.

## Example: a wrapping, centered row that won't overflow its panel

```kotlin
panel.layout = RowLayout(wrap = true, justify = JustifyContent.CENTER)
panel.layoutBox = LayoutBox(gap = 8f, padding = 8f, alignY = Align.CENTER)
```

## Example: a 3-column auto-stretched grid

```kotlin
grid.layout = GridLayout(columns = 3, stretch = true, alignItems = AlignItems.CENTER)
```

## Clipping

`Component.clipChildren: Boolean` (default `false`) clips children to the component's own bounds when rendering — `PanelWidget` sets this to `true` by default, so a `RowLayout`/`GridLayout` with more/bigger children than fit gets visually cut off at the panel edge instead of spilling past it. Set `panel.clipChildren = false` to opt out.
