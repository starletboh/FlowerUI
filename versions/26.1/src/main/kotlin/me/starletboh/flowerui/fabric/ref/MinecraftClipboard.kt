package me.starletboh.flowerui.fabric.ref

import me.starletboh.flowerui.ref.Clipboard
import net.minecraft.client.Minecraft


class MinecraftClipboard : Clipboard {

    override fun get(): String? {
        return Minecraft.getInstance().keyboardHandler.clipboard
    }

    override fun set(value: String) {
        Minecraft.getInstance().keyboardHandler.clipboard = value
    }
}