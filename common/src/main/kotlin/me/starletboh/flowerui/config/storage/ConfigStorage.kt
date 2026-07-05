package me.starletboh.flowerui.config.storage

import me.starletboh.flowerui.config.FlowerConfig

interface ConfigStorage {

    fun load(): FlowerConfig?

    fun save(config: FlowerConfig)
}