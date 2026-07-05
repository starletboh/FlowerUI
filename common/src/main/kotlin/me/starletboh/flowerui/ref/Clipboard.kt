package me.starletboh.flowerui.ref

interface Clipboard {
    fun get(): String?
    fun set(value: String)
}