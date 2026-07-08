package me.starletboh.flowerui.fabric.graphics

import net.minecraft.client.MinecraftClient
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.util.Identifier
import me.starletboh.flowerui.graphics.backend.TextureBackend
import java.awt.image.BufferedImage

class FabricTextureBackend : TextureBackend {

    override fun registerTexture(id: String, image: BufferedImage): Identifier {
        val nativeImage = convert(image)
        val texture = NativeImageBackedTexture(id::toString, nativeImage)


        val identifier = if (id.contains(":")) {
            val parts = id.split(":", limit = 2)
            Identifier.of(parts[0].lowercase(), parts[1].lowercase())
        } else {

            Identifier.of("flowerui", id.lowercase())
        }

        MinecraftClient.getInstance()
            .textureManager
            .registerTexture(identifier, texture)

        return identifier
    }

    private fun convert(image: BufferedImage): NativeImage {
        val native = NativeImage(image.width, image.height, false)

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val argb = image.getRGB(x, y)

                val a = (argb shr 24) and 0xFF
                val r = (argb shr 16) and 0xFF
                val g = (argb shr 8) and 0xFF
                val b = argb and 0xFF

                native.setColorArgb(x, y, (a shl 24) or (r shl 16) or (g shl 8) or b)
            }
        }

        return native
    }
}