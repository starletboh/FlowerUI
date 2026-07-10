plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("maven-publish")
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}
val shadowImpl by configurations.creating
version = "1.0.0+mc1.21.9"
tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
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