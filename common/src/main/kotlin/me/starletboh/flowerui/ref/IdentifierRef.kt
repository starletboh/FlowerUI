package me.starletboh.flowerui.ref

interface IdentifierRef {

    fun of(namespace: String, path: String): Any

    fun ofVanilla(path: String): Any

    fun ofFlowerUI(path: String): Any



}