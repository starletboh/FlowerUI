package me.starletboh.flowerui.graphics.backend

import java.awt.image.BufferedImage

class DummyTextureBackend : TextureBackend {

    override fun registerTexture(id: String, image: BufferedImage): Any {

        println("Registered texture: $id (${image.width}x${image.height})")
        return id
    }
}