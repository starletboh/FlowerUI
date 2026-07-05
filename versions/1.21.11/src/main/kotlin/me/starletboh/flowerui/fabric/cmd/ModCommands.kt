package me.starletboh.flowerui.fabric.cmd

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback

object ModCommands {

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            ExamplesCommand.register(dispatcher)
            AboutCommand.register(dispatcher)
        }
    }
}