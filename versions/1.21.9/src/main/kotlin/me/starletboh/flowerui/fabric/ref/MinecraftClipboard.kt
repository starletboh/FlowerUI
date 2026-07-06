package me.starletboh.flowerui.fabric.ref

import me.starletboh.flowerui.ref.Clipboard
import net.minecraft.client.MinecraftClient

class MinecraftClipboard : Clipboard {

    override fun get(): String? {
        return MinecraftClient.getInstance().keyboard.clipboard
    }

    override fun set(value: String) {
        MinecraftClient.getInstance().keyboard.clipboard = value
    }
}