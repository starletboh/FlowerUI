plugins {
    kotlin("jvm") version "2.4.0" apply false
    `maven-publish`
}

evaluationDependsOnChildren()

allprojects {
    group = "me.starletboh.flowerui"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://jitpack.io")
    }
}

tasks.register("buildAll") {
    group = "build"

    dependsOn(provider {
        subprojects.filter { it.name != "common" && it.subprojects.isEmpty() }.map { "${it.path}:build" }
    })

    doLast {
        val out = layout.buildDirectory.dir("allJars").get().asFile
        out.mkdirs()

        subprojects.filter { it.name != "common" }.forEach { p ->

            val targetTaskName = if (p.tasks.findByName("remapJar") != null) "remapJar" else "shadowJar"
            val task = p.tasks.findByName(targetTaskName)

            if (task != null) {
                try {

                    val archiveFileProp = task.property("archiveFile") as? org.gradle.api.file.RegularFileProperty
                    val jarFile = archiveFileProp?.get()?.asFile

                    if (jarFile != null && jarFile.exists()) {
                        val finalComponentVersion = p.providers.gradleProperty("mod_version").orNull ?: p.version.toString()
                        val targetFile = java.io.File(out, "${p.name}-$finalComponentVersion.jar")

                        jarFile.copyTo(targetFile, overwrite = true)
                        println("Successfully copied built mod jar from ${p.name} ($targetTaskName): ${targetFile.name}")
                    }
                } catch (e: Exception) {
                    println("Skipped copying for ${p.name} due to error: ${e.message}")
                }
            }
        }
    }
}