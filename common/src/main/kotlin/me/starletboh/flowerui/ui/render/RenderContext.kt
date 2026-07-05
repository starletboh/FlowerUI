package me.starletboh.flowerui.ui.render

import me.starletboh.flowerui.config.FlowerConfig
import me.starletboh.flowerui.theme.ThemeDefinition
import me.starletboh.flowerui.animation.AnimationEngine
import me.starletboh.flowerui.ui.render.RenderScope
import me.starletboh.flowerui.config.ConfigManager
import me.starletboh.flowerui.theme.ThemeContext

data class RenderContext(

    // --------------------------------------------------
    // FRAME DATA
    // --------------------------------------------------

    val deltaTime: Float,
    val time: Long,

    // --------------------------------------------------
    // INPUT SNAPSHOT
    // --------------------------------------------------

    val mouseX: Double,
    val mouseY: Double,
    val mouseDown: Boolean,
    val mouseButton: Int,
    val pressedKeys: Set<Int> = emptySet(),

    // --------------------------------------------------
    // RENDER SYSTEM CORE (IMPORTANT)
    // --------------------------------------------------

    val scope: RenderScope,

    // --------------------------------------------------
    // UI CONFIG + THEME
    // --------------------------------------------------

    val theme: ThemeDefinition,
    val config: FlowerConfig = ConfigManager.config,
    val scale: Float,

    // --------------------------------------------------
    // ENGINE ACCESS
    // --------------------------------------------------

    val animationEngine: AnimationEngine = AnimationEngine
)