pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("net.fabricmc.fabric-loom-remap") version providers.gradleProperty("loom_version")
    }
}

rootProject.name = "FlowerUI"

include(":common")

file("versions").listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { dir ->
        include(":versions:${dir.name}")
        project(":versions:${dir.name}").projectDir = dir
    }

include(":common")

file("versions").listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { dir ->
        include(":versions:${dir.name}")
        project(":versions:${dir.name}").projectDir = dir
    }
