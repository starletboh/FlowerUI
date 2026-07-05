package me.starletboh.flowerui.animation

object AnimationEngine {

    private val tweens = mutableSetOf<Tween>()

    private var lastDelta: Float = 0f

    fun register(tween: Tween) {
        tweens.add(tween)
    }

    fun unregister(tween: Tween) {
        tweens.remove(tween)
    }

    /**
     * Called once per frame from Fabric/Minecraft tick or render loop
     */
    fun tick(delta: Float) {
        lastDelta = delta

        // safe iteration (prevents modification crash if tweens register/unregister mid-frame)
        val snapshot = tweens.toList()

        for (tween in snapshot) {
            tween.update(delta)
        }
    }

    /**
     * Optional: global access if needed by UI system
     */
    fun getLastDelta(): Float = lastDelta

    /**
     * Clears all animations (useful when switching screens)
     */
    fun clear() {
        tweens.clear()
    }
}