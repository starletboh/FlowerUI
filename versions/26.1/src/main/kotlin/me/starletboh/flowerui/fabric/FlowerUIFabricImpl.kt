package me.starletboh.flowerui.fabric

import me.starletboh.flowerui.api.FlowerUI
import me.starletboh.flowerui.api.FlowerUIPlatform
import me.starletboh.flowerui.animation.AnimationEngine
import me.starletboh.flowerui.config.ConfigManager
import me.starletboh.flowerui.config.migration.ConfigPatchManager
import me.starletboh.flowerui.config.migration.patches.ThemeNormalizationPatch
import me.starletboh.flowerui.config.migration.patches.UIScalePatch
import me.starletboh.flowerui.config.storage.FileConfigStorage
import me.starletboh.flowerui.fabric.cmd.ModCommands

import me.starletboh.flowerui.fabric.graphics.FabricTextureBackend
import me.starletboh.flowerui.fabric.ref.MinecraftClipboard
import me.starletboh.flowerui.fabric.screen.FlowerUIScreen
import me.starletboh.flowerui.graphics.backend.GraphicsBackend
import me.starletboh.flowerui.graphics.svg.SvgTextureManager
import me.starletboh.flowerui.ref.PlatformServices
import me.starletboh.flowerui.theme.BuiltInThemes
import me.starletboh.flowerui.ui.registry.WidgetRegistry

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft

import java.io.File

object FlowerUIFabricImpl : FlowerUIPlatform {

    private var initialized = false
    private var openedDemo = false
    private val mc = Minecraft.getInstance()
    fun init() {

        val configFile = File("config/flowerui.json")
        ConfigManager.init(FileConfigStorage(configFile))

        registerPatches()

        FlowerUI.init(this)
        PlatformServices.clipboard = MinecraftClipboard()
        ModCommands.register()
        ClientTickEvents.END_CLIENT_TICK.register {



            if (!initialized) {

                val backend = FabricTextureBackend()

                GraphicsBackend.init(backend)
                SvgTextureManager.setBackend(backend)

                registerBuiltinWidgets()
                registerBuiltinThemes()

                initialized = true
            }

            AnimationEngine.tick(1f / 60f)


        }
    }
    override fun close() {
        mc.execute {
            mc.setScreen(null)
        }
    }
    override fun open(screen: me.starletboh.flowerui.ui.FlowerScreen) {

        mc.execute {
        mc.setScreen(
            FlowerUIScreen(screen)
        )}
    }


    private fun registerPatches() {
        ConfigPatchManager.register(ThemeNormalizationPatch())
        ConfigPatchManager.register(UIScalePatch())
    }

    private fun registerBuiltinWidgets() {
        WidgetRegistry.register("button") {
            me.starletboh.flowerui.ui.widgets.ButtonWidget()
        }
        WidgetRegistry.register("panel") {
            me.starletboh.flowerui.ui.widgets.PanelWidget()
        }
        WidgetRegistry.register("text") {
            me.starletboh.flowerui.ui.widgets.TextWidget()
        }
        WidgetRegistry.register("checkbox") {
            me.starletboh.flowerui.ui.widgets.CheckboxWidget()
        }
        WidgetRegistry.register("text_input") {
            me.starletboh.flowerui.ui.widgets.TextInputWidget()
        }
        WidgetRegistry.register("color_picker") {
            me.starletboh.flowerui.ui.widgets.ColorPickerWidget()
        }
        WidgetRegistry.register("image") {
            me.starletboh.flowerui.ui.widgets.ImageWidget()
        }
        WidgetRegistry.register("toggle_switch") {
            me.starletboh.flowerui.ui.widgets.ToggleWidget()
        }
        WidgetRegistry.register("slider") {
            me.starletboh.flowerui.ui.widgets.SliderWidget()
        }

    }

    private fun registerBuiltinThemes() {
        BuiltInThemes.registerAll()
    }
}