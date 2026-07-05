package me.starletboh.flowerui.config

data class FlowerConfig(
    val version: Int = 1,
    val appliedPatches: Set<String> = emptySet(),

    val ui: UiConfig = UiConfig(),
    val theme: ThemeConfig = ThemeConfig(),
    val animation: AnimationConfig = AnimationConfig()
)