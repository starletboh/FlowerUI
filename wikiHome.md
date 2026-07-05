# FlowerUI

**FlowerUI** is a lightweight, themeable UI framework for Fabric mods. It exists so mod authors can build custom in-game screens — settings menus, HUD editors, dashboards, pickers — in plain Kotlin, without writing a `Screen` subclass and hand-rolling `ClickableWidget`s and raw `DrawContext` calls for every element.

Instead of imperative rendering code, you describe a tree of widgets (panels, buttons, text, checkboxes, sliders, dropdowns, scrollable lists, a full color picker, etc.), attach a layout to each container, and FlowerUI handles measuring, positioning, clipping, input routing, and theming for you.

## Why it exists

Writing GUI code directly against Minecraft/Fabric's `Screen`/`DrawContext` API is repetitive and low-level: every button needs manual bounds-checking, every scrollable area needs manual scissor math, and nothing is themeable without threading color values through every draw call by hand. FlowerUI wraps that once, in `common`, so your mod's screen code reads like UI code rather than rendering code.

## Core ideas

- **Widget tree, not draw calls.** Screens are built by composing `Component`/`Widget` instances (`PanelWidget`, `ButtonWidget`, `TextWidget`, …) into a tree, similar in spirit to how you'd build UI in most modern UI toolkits.
- **A real layout system.** `RowLayout`, `ColumnLayout`, `GridLayout`, and `FlexLayout` all implement a two-phase **measure → arrange** contract: a container can either be given a fixed size, or left to size itself to its content, and children get positioned (and optionally stretched/wrapped/justified) inside whatever size the container resolves to.
- **Theming is first-class.** Widgets never hardcode colors — they read from `ctx.theme.colors` at render time. Swap the active `ThemeDefinition` and every screen re-skins itself automatically. Ships with a few presets (Catppuccin Mocha, Flower Dark, Flower Light) and supports registering your own.
- **Multi-version by design.** All the logic that doesn't touch Minecraft's rendering API lives in `common`. Each supported Minecraft version gets a thin adapter module under `versions/<version>` that implements the actual `DrawContext`-backed drawing and input plumbing.

## What's included

- Layouts: `RowLayout` (with wrap + justify-content), `ColumnLayout`, `GridLayout` (auto-fit columns, per-item align/stretch), `FlexLayout`.
- Widgets: `PanelWidget`, `ButtonWidget`, `TextWidget`, `CheckboxWidget`, `ToggleWidget`, `SliderWidget`, `DropdownWidget`, `TextInputWidget`, `ColorPickerWidget` (full HSV + alpha picker), `ImageWidget`, `ScrollContainer`.
- A theming system (`ThemeRegistry`, `ThemeDefinition`, built-in presets) that every widget reads from automatically.
- A config system with a migration/patch pipeline, so saved settings can be upgraded across mod versions instead of breaking.
- SVG-backed texture generation with a bounded LRU cache, used internally for rounded rects, gradients, and other generated graphics.

## Where to go next

- **[Getting Started](Getting-Started)** — add FlowerUI to your mod and build your first screen.
- **[Layout System](Layout-System)** — how measure/arrange works and how each layout behaves.
- **[Widget Reference](Widget-Reference)** — every built-in widget and its properties.
- **[Overlays](Overlays)** — how popover-style content (like an open dropdown) escapes a clipped container and stays clickable.
- **[Theming](Theming)** — how colors are resolved and how to add your own theme.

## Project status

FlowerUI is under active development and currently targets **Minecraft 1.21.11** (Fabric). The API may still change between versions until a stable release is tagged.
