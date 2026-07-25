plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}
val shadowImpl by configurations.creating
version = "1.0.2-mc1.21.9"
tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.properties["minecraft_version"])
    inputs.property("loader_version", project.properties["loader_version"])
    inputs.property("kotlin_loader_version", project.properties["kotlin_loader_version"])
//    inputs.property("flower_ui_version", project.properties["flower_ui_version"])
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version, "loader_version" to project.properties["loader_version"] as String,
            "minecraft_version" to project.properties["minecraft_version"] as String,
//            "loader_version" to project.property("loader_version")
            "kotlin_loader_version" to project.properties["kotlin_loader_version"] as String,
//           "flower_ui_version" to project.properties["flower_ui_version"] as String
        )
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")

    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["kotlin_loader_version"]}")

    implementation(project(":common"))


    val jackson = "3.1.3"
    modImplementation("tools.jackson.core:jackson-databind:$jackson")


    shadowImpl("tools.jackson.core:jackson-databind:$jackson")
    modImplementation("tools.jackson.core:jackson-core:$jackson")

    shadowImpl("tools.jackson.core:jackson-core:$jackson")
    modImplementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    shadowImpl("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    modImplementation("com.formdev:svgSalamander:1.1.4")

    shadowImpl("com.formdev:svgSalamander:1.1.4")


}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.shadowJar {
    archiveClassifier.set("dev-shadow")


    configurations = listOf(shadowImpl)


    from(project(":common").sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    archiveClassifier.set("")
}
publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = "FlowerUI-Fabric"
            artifact(tasks.remapJar)
        }
    }
}