package me.starletboh.flowerui.fabric

import net.fabricmc.api.ClientModInitializer

class FlowerUIFabric : ClientModInitializer {

    override fun onInitializeClient() {
        FlowerUIFabricImpl.init()
    }
}