package me.starletboh.flowerui.graphics.svg.generator

object SvgBuilder {

    fun roundedRect(
        width: Int,
        height: Int,
        radius: Int,
        fill: String
    ): String {

        return """
            <svg xmlns="http://www.w3.org/2000/svg"
                 width="$width"
                 height="$height">

                <rect width="$width"
                      height="$height"
                      rx="$radius"
                      ry="$radius"
                      fill="$fill"/>
            </svg>
        """.trimIndent()
    }

    /** Vertical rainbow strip (top = hue 0, bottom = hue 360, wrapping back to red) for a hue picker. */
    fun hueBarVertical(width: Int, height: Int): String {
        val stops = (0..6).joinToString("\n") { i ->
            val hue = i / 6f
            val color = java.awt.Color.HSBtoRGB(hue, 1f, 1f) and 0xFFFFFF
            val hex = String.format("#%06X", color)
            val offset = (i / 6f * 100)
            "<stop offset=\"$offset%\" stop-color=\"$hex\"/>"
        }

        return """
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height">
              <defs>
                <linearGradient id="hue" x1="0" y1="0" x2="0" y2="1">
                  $stops
                </linearGradient>
              </defs>
              <rect width="$width" height="$height" fill="url(#hue)"/>
            </svg>
        """.trimIndent()
    }

    /**
     * A saturation (left->right, white->hue) / value (top->bottom,
     * transparent->black) square for a given pure [hueHex] (e.g. from
     * `Color.HSBtoRGB(hue, 1f, 1f)`).
     */
    fun saturationValueSquare(width: Int, height: Int, hueHex: String): String {
        return """
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height">
              <defs>
                <linearGradient id="sat" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stop-color="#FFFFFF"/>
                  <stop offset="100%" stop-color="$hueHex" stop-opacity="0"/>
                </linearGradient>
                <linearGradient id="val" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="0%" stop-color="#000000" stop-opacity="0"/>
                  <stop offset="100%" stop-color="#000000"/>
                </linearGradient>
              </defs>
              <rect width="$width" height="$height" fill="$hueHex"/>
              <rect width="$width" height="$height" fill="url(#sat)"/>
              <rect width="$width" height="$height" fill="url(#val)"/>
            </svg>
        """.trimIndent()
    }

    /** Horizontal transparent -> opaque gradient of [colorHex], for an alpha slider. */
    fun alphaBarHorizontal(width: Int, height: Int, colorHex: String): String {
        return """
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height">
              <defs>
                <linearGradient id="a" x1="0" y1="0" x2="1" y2="0">
                  <stop offset="0%" stop-color="$colorHex" stop-opacity="0"/>
                  <stop offset="100%" stop-color="$colorHex" stop-opacity="1"/>
                </linearGradient>
              </defs>
              <rect width="$width" height="$height" fill="url(#a)"/>
            </svg>
        """.trimIndent()
    }

    /** Grey/white checkerboard, used as a backdrop so translucent colors are visible. */
    fun checkerboard(width: Int, height: Int, cell: Int = 4): String {
        val c2 = cell * 2
        return """
            <svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height">
              <defs>
                <pattern id="chk" width="$c2" height="$c2" patternUnits="userSpaceOnUse">
                  <rect width="$c2" height="$c2" fill="#FFFFFF"/>
                  <rect width="$cell" height="$cell" fill="#CCCCCC"/>
                  <rect x="$cell" y="$cell" width="$cell" height="$cell" fill="#CCCCCC"/>
                </pattern>
              </defs>
              <rect width="$width" height="$height" fill="url(#chk)"/>
            </svg>
        """.trimIndent()
    }
}