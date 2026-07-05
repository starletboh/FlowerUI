package me.starletboh.flowerui.api

import me.starletboh.flowerui.ui.FlowerScreen

interface FlowerUIPlatform {
    fun open(screen: FlowerScreen)
    fun close()
}