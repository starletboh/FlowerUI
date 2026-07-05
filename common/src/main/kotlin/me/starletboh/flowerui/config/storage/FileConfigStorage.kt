package me.starletboh.flowerui.config.storage

import me.starletboh.flowerui.config.FlowerConfig
import me.starletboh.flowerui.config.migration.ConfigPatchManager
import tools.jackson.databind.ObjectMapper
import java.io.File

class FileConfigStorage(
    private val file: File
) : ConfigStorage {

    private val mapper = ObjectMapper()

    override fun load(): FlowerConfig? {
        if (!file.exists()) return null

        return try {
            val config = mapper.readValue(file, FlowerConfig::class.java)

            val patched = ConfigPatchManager.applyAll(config)

            if (patched != config) {
                save(patched)
            }

            patched

        } catch (e: Exception) {
            println("[FlowerUI] Config corrupted → resetting")

            val fresh = FlowerConfig()
            save(fresh)
            fresh
        }
    }

    override fun save(config: FlowerConfig) {
        file.parentFile?.mkdirs()

        mapper.writerWithDefaultPrettyPrinter()
            .writeValue(file, config)
    }

    private fun validate(config: FlowerConfig) {
        require(config.theme.currentThemeId.isNotBlank())
    }

    private fun backupBrokenConfig() {
        if (!file.exists()) return

        val backup = File(file.parentFile, "flowerui.broken.json")
        file.copyTo(backup, overwrite = true)
    }
}