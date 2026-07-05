package me.starletboh.flowerui.animation

class Tween(
    var value: Float = 0f
) {

    private var start = 0f
    private var end = 0f

    private var duration = 0.3f
    private var time = 0f

    private var easing = EasingType.LINEAR

    private var running = false

    fun to(target: Float, duration: Float, easing: EasingType) {
        start = value
        end = target
        this.duration = duration
        this.easing = easing
        time = 0f
        running = true
    }

    fun update(delta: Float) {

        if (!running) return

        time += delta
        val t = (time / duration).coerceIn(0f, 1f)

        val eased = applyEasing(t, easing)

        value = start + (end - start) * eased

        if (t >= 1f) {
            value = end
            running = false
        }
    }

    private fun applyEasing(t: Float, type: EasingType): Float {
        return when (type) {
            EasingType.LINEAR -> t
            EasingType.EASE_IN -> t * t
            EasingType.EASE_OUT -> 1f - (1f - t) * (1f - t)
            EasingType.EASE_IN_OUT -> if (t < 0.5f)
                2f * t * t
            else
                1f - (-2f * t + 2f).let { it * it / 2f }
        }
    }
}