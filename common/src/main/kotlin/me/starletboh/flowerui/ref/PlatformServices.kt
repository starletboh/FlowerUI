package me.starletboh.flowerui.ref



import me.starletboh.flowerui.graphics.backend.TextureBackend


object PlatformServices {
    lateinit var clipboard: Clipboard
    lateinit var identifier: IdentifierRef
    lateinit var textureBackend: TextureBackend
    lateinit var utils: UtilRef
}