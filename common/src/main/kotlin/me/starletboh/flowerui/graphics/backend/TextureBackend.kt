package me.starletboh.flowerui.graphics.backend

import java.awt.image.BufferedImage

interface TextureBackend {

    fun registerTexture(
        id: String,
        image: BufferedImage
    ): Any
}