pluginManagement {
    repositories {
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
    // CRITICAL: Left empty so subprojects are free to apply either
    // "net.fabricmc.fabric-loom" OR "net.fabricmc.fabric-loom-remap"
    // directly inside their own build.gradle.kts files.
    plugins {}
}

rootProject.name = "FlowerUI"

// Include your shared base module
include(":common")

// Cleaned single loop that dynamically registers folders under /versions
file("versions").listFiles()
    ?.filter { it.isDirectory }
    ?.forEach { dir ->
        val projectPath = ":versions:${dir.name}"
        include(projectPath)
        project(projectPath).projectDir = dir
    }