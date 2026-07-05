package me.starletboh.flowerui.config

import me.starletboh.flowerui.config.storage.ConfigStorage

object ConfigManager {

    private var storage: ConfigStorage? = null

    var config: FlowerConfig = FlowerConfig()
        private set

    fun init(storage: ConfigStorage) {
        this.storage = storage
        load()
    }

    fun load() {
        config = storage?.load() ?: FlowerConfig()
    }

    fun save() {
        storage?.save(config)
    }
}