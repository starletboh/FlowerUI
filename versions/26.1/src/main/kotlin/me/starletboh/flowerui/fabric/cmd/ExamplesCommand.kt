package me.starletboh.flowerui.fabric.cmd

import com.mojang.brigadier.CommandDispatcher
import me.starletboh.flowerui.api.FlowerUI

import me.starletboh.flowerui.fabric.example.ExampleScreen

import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource


object ExamplesCommand {

    fun register(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        dispatcher.register(
            ClientCommands.literal("flowerui:example")
                .executes { context ->


                        FlowerUI.open(ExampleScreen())
                    1

                }
        )
    }
}