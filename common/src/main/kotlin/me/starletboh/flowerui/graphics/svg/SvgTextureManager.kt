package me.starletboh.flowerui.graphics.svg

import com.kitfox.svg.SVGUniverse
import me.starletboh.flowerui.graphics.backend.GraphicsBackend
import me.starletboh.flowerui.graphics.backend.TextureBackend
import me.starletboh.flowerui.graphics.texture.TextureHandle
import java.awt.RenderingHints
import java.awt.image.BufferedImage

object SvgTextureManager {

    // Bounded + LRU (accessOrder = true) so long-running screens that
    // constantly resize/recolor widgets (animations, draggable panels,
    // hover states, etc.) can't grow this map forever - the least
    // recently used entries get evicted once the cap is hit instead of
    // every unique width/height/color combination living forever.
    private const val RENDER_SCALE = 2
    private const val MAX_CACHE_ENTRIES = 512

    private val cache = object : LinkedHashMap<String, TextureHandle>(64, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, TextureHandle>): Boolean {
            return size > MAX_CACHE_ENTRIES
        }
    }

    lateinit var backend: TextureBackend
        private set

    fun setBackend(backend: TextureBackend) {
        this.backend = backend
    }
    private fun safeId(input: String): String {
        return input
            .lowercase()
            .replace(Regex("[^a-z0-9/._-]"), "_")
    }
    private fun createUniverse(): SVGUniverse = SVGUniverse()

    fun getSvgTexture(
        id: String,
        svg: String,
        width: Int,
        height: Int
    ): TextureHandle {

        val cacheKey = "$id|$width|$height|$svg"

        synchronized(cache) {
            cache[cacheKey]?.let { return it }
        }

        val image = renderSvg(svg, width, height)


        val safeId = safeId(id)

        val texture = GraphicsBackend.registerTexture(safeId, image)

        synchronized(cache) {
            cache[cacheKey] = texture
        }
        return texture
    }

//    private fun renderSvg(
//        svg: String,
//        width: Int,
//        height: Int
//    ): BufferedImage {
//
//        val universe = createUniverse()
//
//        val uri = universe.loadSVG(svg.byteInputStream(), "flowerui_${System.nanoTime()}")
//        val diagram = universe.getDiagram(uri)
//            ?: throw IllegalStateException("SVG failed to load: $uri")
//
//        val svgW = (diagram.width.toDouble().takeIf { it > 0 } ?: width.toDouble())
//        val svgH = (diagram.height.toDouble().takeIf { it > 0 } ?: height.toDouble())
//
//        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
//        val g = image.createGraphics()
//
//        try {
//            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
//            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
//
//            g.scale(width / svgW, height / svgH)
//            diagram.render(g)
//
//        } finally {
//            g.dispose()
//        }
//
//        return image
//    }
private fun renderSvg(
    svg: String,
    width: Int,
    height: Int
): BufferedImage {

    val renderWidth = width * RENDER_SCALE
    val renderHeight = height * RENDER_SCALE

    val universe = createUniverse()

    val uri = universe.loadSVG(
        svg.byteInputStream(),
        "flowerui_${System.nanoTime()}"
    )

    val diagram = universe.getDiagram(uri)
        ?: throw IllegalStateException("SVG failed to load: $uri")

    val svgW = diagram.width.toDouble().takeIf { it > 0 }
        ?: width.toDouble()

    val svgH = diagram.height.toDouble().takeIf { it > 0 }
        ?: height.toDouble()


    val highRes = BufferedImage(
        renderWidth,
        renderHeight,
        BufferedImage.TYPE_INT_ARGB
    )

    val g = highRes.createGraphics()

    try {
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )

        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        )

        g.scale(
            renderWidth / svgW,
            renderHeight / svgH
        )

        diagram.render(g)

    } finally {
        g.dispose()
    }


    // Downsample back to normal size
    val finalImage = BufferedImage(
        width,
        height,
        BufferedImage.TYPE_INT_ARGB
    )

    val downscale = finalImage.createGraphics()

    try {
        downscale.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        )

        downscale.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY
        )

        downscale.drawImage(
            highRes,
            0,
            0,
            width,
            height,
            null
        )

    } finally {
        downscale.dispose()
    }

    return finalImage
}
}