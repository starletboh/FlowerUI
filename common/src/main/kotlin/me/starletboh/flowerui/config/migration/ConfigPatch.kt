package me.starletboh.flowerui.config.migration

import me.starletboh.flowerui.config.FlowerConfig

interface ConfigPatch {

    /**
     * Unique ID so patches don't run twice
     */
    val id: String

    /**
     * Apply transformation safely
     */
    fun apply(config: FlowerConfig): FlowerConfig
}