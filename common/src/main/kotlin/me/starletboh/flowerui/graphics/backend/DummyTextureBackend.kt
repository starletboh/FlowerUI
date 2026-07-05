package me.starletboh.flowerui.graphics.backend

import java.awt.image.BufferedImage

class DummyTextureBackend : TextureBackend {

    override fun registerTexture(id: String, image: BufferedImage): Any {
        // TODO: replace with Fabric texture upload
        println("Registered texture: $id (${image.width}x${image.height})")
        return id
    }
}