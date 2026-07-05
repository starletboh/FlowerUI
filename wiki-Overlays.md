# Overlays

Some widgets need part of themselves to render **on top of everything else**, and stay clickable there, regardless of where they logically sit in the tree — the canonical example is an open dropdown's item list, which needs to escape whatever container (often a clipped `PanelWidget`) it's placed inside.

Two problems make this not "just work" by default:

1. **Clipping.** Any ancestor with `clipChildren = true` (e.g. `PanelWidget`, on by default) clips everything drawn as part of its normal child render pass to its own bounds. Content meant to spill outside that box (like a dropdown list below a panel's bottom edge) gets cut off.
2. **Hit-testing.** `Component.hitTestDeep` requires `contains(x, y)` to be true for a widget *before* it will even check that widget's children — and `contains()` only checks the widget's own `width`/`height` box. Content rendered outside that box is therefore also outside what the tree-based hit-test will ever route a click to, no matter how it's drawn.

## The `Overlay` interface

```kotlin
interface Overlay {
    fun renderOverlay(ctx: RenderContext)
    fun overlayHitTest(x: Double, y: Double): Component?
}
```

`RootComponent` keeps a separate list of currently-shown overlays:

```kotlin
fun showOverlay(overlay: Overlay)
fun hideOverlay(overlay: Overlay)
```

Registering a widget as an overlay **doesn't remove it from the normal tree** — it's still added via `parent.add(...)` as usual, and still renders/positions itself normally through `render()`/`applyLayout()`. `showOverlay`/`hideOverlay` just ask `RootComponent` to *additionally*:

- call `renderOverlay(ctx)` after all normal children have rendered (so it draws on top, unclipped by any ancestor), and
- check `overlayHitTest(x, y)` before falling back to the normal tree-based hit-test (so overlay content is clickable even though it's outside the widget's own bounding box).

## How `DropdownWidget` uses it

- `render()` always draws just the collapsed box — normal child rendering, clipped like everything else, which is fine since the box itself doesn't need to escape its container.
- On click, `openMenu()` sets `isOpen = true` and calls `findRoot()?.showOverlay(this)`.
- `renderOverlay(ctx)` draws the item list (only called while open).
- `overlayHitTest(x, y)` returns `this` if the click lands on one of the item rows, `null` otherwise.
- It auto-closes (`closeMenu()`, which also calls `hideOverlay(this)`) whenever `InputRouter.currentFocus !== this` — i.e. as soon as focus moves elsewhere, such as clicking away.

## Using it in your own widget

If you're building a popover/tooltip/context-menu-style widget of your own:

```kotlin
class MyPopover : Widget(), Overlay {

    private var isOpen = false

    override fun render(ctx: RenderContext) {
        // draw the always-visible trigger part here
    }

    override fun renderOverlay(ctx: RenderContext) {
        if (!isOpen) return
        // draw the popover content here - runs unclipped, on top
    }

    override fun overlayHitTest(x: Double, y: Double): Component? {
        return if (isOpen && /* (x, y) is within your popover's drawn bounds */ true) this else null
    }

    override fun onEvent(event: InputEvent): Boolean {
        // toggle isOpen + call findRoot()?.showOverlay(this) / hideOverlay(this)
        return false
    }
}
```

`findRoot()` (on `Component`) walks up the parent chain to find the `RootComponent` — it returns `null` if the widget isn't attached to a screen yet, so guard for that the same way `DropdownWidget` does (`findRoot()?.showOverlay(this)`).

### Z-order with multiple overlays

If more than one overlay is shown at once, `RootComponent` draws (and hit-tests) them in the order they were shown — the most recently shown one wins on both counts. There's currently no explicit z-index; if you need one widget's popover to always sit above another's, control that via show/hide order rather than a priority field.
