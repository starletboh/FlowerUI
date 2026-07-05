package me.starletboh.flowerui.config.migration.patches

import me.starletboh.flowerui.config.FlowerConfig
import me.starletboh.flowerui.config.migration.ConfigPatch

class UIScalePatch : ConfigPatch {

    override val id = "ui_scale_default_v1"

    override fun apply(config: FlowerConfig): FlowerConfig {

        val ui = config.ui

        if (ui.scale <= 0f) {
            return config.copy(
                ui = ui.copy(scale = 1.0f)
            )
        }

        return config
    }
}