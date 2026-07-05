package me.starletboh.flowerui.graphics.backend

import me.starletboh.flowerui.graphics.texture.TextureHandle
import java.awt.image.BufferedImage
object GraphicsBackend {

    private var _backend: TextureBackend? = null

    val backend: TextureBackend
        get() = _backend ?: error("GraphicsBackend not initialized! Call GraphicsBackend.init(...) in onInitializeClient")

    fun init(backend: TextureBackend) {
        _backend = backend
    }

    fun registerTexture(id: String, image: BufferedImage): TextureHandle {
        return backend.registerTexture(id, image)
    }
}