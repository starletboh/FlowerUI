package me.starletboh.flowerui.fabric.cmd

import com.mojang.brigadier.CommandDispatcher
import me.starletboh.flowerui.api.FlowerUI
import me.starletboh.flowerui.fabric.example.AboutScreen
import me.starletboh.flowerui.fabric.example.ExampleScreen
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.client.MinecraftClient

object ExamplesCommand {

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommandManager.literal("flowerui:example")
                .executes { context ->
                    // TODO

                        FlowerUI.open(ExampleScreen())
                    1

                }
        )
    }
}