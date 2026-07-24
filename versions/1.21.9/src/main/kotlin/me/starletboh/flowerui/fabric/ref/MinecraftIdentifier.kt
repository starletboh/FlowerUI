package me.starletboh.flowerui.fabric.ref

import me.starletboh.flowerui.ref.IdentifierRef
import net.minecraft.util.Identifier

class MinecraftIdentifier : IdentifierRef {
    override fun of(namespace: String, path: String): Identifier {
        return Identifier.of(namespace, path)
    }

    override fun ofVanilla(path: String): Identifier {
        return Identifier.ofVanilla(path)
    }

    override fun ofFlowerUI(path: String): Identifier {
        return Identifier.of("flowerui", path)
    }
}