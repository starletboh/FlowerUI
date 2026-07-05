package me.starletboh.flowerui.config.migration

import me.starletboh.flowerui.config.FlowerConfig

object ConfigPatchManager {

    private val patches = mutableListOf<ConfigPatch>()

    fun register(patch: ConfigPatch) {
        patches += patch
    }

    fun applyAll(config: FlowerConfig): FlowerConfig {

        var result = config
        var applied = config.appliedPatches.toMutableSet()

        for (patch in patches) {

            if (patch.id in applied) continue

            result = patch.apply(result)
            applied += patch.id
        }

        return result.copy(appliedPatches = applied)
    }
}