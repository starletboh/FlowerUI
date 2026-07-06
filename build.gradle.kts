plugins {
    kotlin("jvm") version "2.1.20" apply false
    `maven-publish`
}

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
    dependsOn(subprojects.map { it.tasks.matching { t -> t.name == "remapJar" } })

    doLast {
        // Use layout.buildDirectory instead of the deprecated buildDir
        val out = layout.buildDirectory.dir("allJars").get().asFile
        out.mkdirs()
        subprojects.forEach { p ->
            p.tasks.findByName("remapJar")?.let { task ->
                val jar = (task as org.gradle.jvm.tasks.Jar).archiveFile.get().asFile
                if (jar.exists()) jar.copyTo(File(out, jar.name), overwrite = true)
            }
        }
    }
}
