package me.starletboh.flowerui.animation.presets

import me.starletboh.flowerui.animation.EasingType
import me.starletboh.flowerui.animation.Tween

object Animations {

    object Presets {

        fun hoverIn(tween: Tween) {
            tween.to(
                target = 1.05f,
                duration = 0.12f,
                easing = EasingType.EASE_OUT
            )
        }

        fun hoverOut(tween: Tween) {
            tween.to(
                target = 1.0f,
                duration = 0.10f,
                easing = EasingType.EASE_IN
            )
        }

        fun pressDown(tween: Tween) {
            tween.to(
                target = 0.92f,
                duration = 0.08f,
                easing = EasingType.EASE_IN
            )
        }

        fun release(tween: Tween) {
            tween.to(
                target = 1.0f,
                duration = 0.12f,
                easing = EasingType.EASE_OUT
            )
        }

        fun fadeIn(tween: Tween) {
            tween.to(
                target = 1.0f,
                duration = 0.18f,
                easing = EasingType.EASE_OUT
            )
        }

        fun fadeOut(tween: Tween) {
            tween.to(
                target = 0.0f,
                duration = 0.15f,
                easing = EasingType.EASE_IN
            )
        }

        fun focusIn(tween: Tween) {
            tween.to(
                target = 1.0f,
                duration = 0.16f,
                easing = EasingType.EASE_OUT
            )
        }

        fun focusOut(tween: Tween) {
            tween.to(
                target = 0.0f,
                duration = 0.14f,
                easing = EasingType.EASE_IN
            )
        }
    }
}