package me.starletboh.flowerui.fabric.graphics

import com.mojang.blaze3d.platform.NativeImage
import me.starletboh.flowerui.graphics.backend.TextureBackend
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.Identifier
import java.awt.image.BufferedImage

class FabricTextureBackend : TextureBackend {

    override fun registerTexture(id: String, image: BufferedImage): Identifier {
        val nativeImage = convert(image)
        val texture = DynamicTexture(id::toString, nativeImage)

        // Split by ':' if present, otherwise default to "flowerui"
        val identifier = if (id.contains(":")) {
            val parts = id.split(":", limit = 2)
            Identifier.fromNamespaceAndPath(parts[0].lowercase(), parts[1].lowercase())
        } else {
            Identifier.fromNamespaceAndPath("flowerui", id.lowercase())
        }

        Minecraft.getInstance().textureManager.register(identifier, texture)
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

                // NativeImage expects colors packed in ABGR format
                val abgr = (a shl 24) or (b shl 16) or (g shl 8) or r

                native.setPixelABGR(x, y, abgr)
            }
        }

        return native
    }
}