package me.starletboh.flowerui.api

import me.starletboh.flowerui.ui.FlowerScreen

object FlowerUI {

    private var platform: FlowerUIPlatform? = null

    fun init(platform: FlowerUIPlatform) {
        this.platform = platform
    }

    fun open(screen: FlowerScreen) {
        platform?.open(screen)
            ?: error("FlowerUI platform not initialized (Fabric not loaded?)")
    }

    fun close() {
        platform?.close()
            ?: error("FlowerUI platform not initialized (Fabric not loaded?)")
    }
}