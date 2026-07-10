import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    `maven-publish`
    id("org.jetbrains.kotlin.jvm") version "2.4.0"
    id("com.gradleup.shadow") version "8.3.0"
}
version = "1.0.0+mc26.1"
val shadowImpl by configurations.creating
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")


   implementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
   implementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_api_version"]}")
   implementation("net.fabricmc:fabric-language-kotlin:${project.properties["kotlin_loader_version"]}")


    implementation(project(":common"))

    // Jackson 3.x block
    val jackson = "3.1.3"
    implementation("tools.jackson.core:jackson-databind:$jackson")

//    include("tools.jackson.core:jackson-databind:$jackson")
    shadowImpl("tools.jackson.core:jackson-databind:$jackson")
    implementation("tools.jackson.core:jackson-core:$jackson")
//    include("tools.jackson.core:jackson-core:$jackson")
    shadowImpl("tools.jackson.core:jackson-core:$jackson")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
//    include("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    shadowImpl("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    // Vector rendering support
    implementation("com.formdev:svgSalamander:1.1.4")
//    include("com.formdev:svgSalamander:1.1.4")
    shadowImpl("com.formdev:svgSalamander:1.1.4")


}

tasks.processResources {
    val version = version
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}


tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    archiveClassifier.set("thin")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    archiveClassifier.set("")

    configurations = listOf(shadowImpl)

    from(project(":common").sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = "FlowerUI-Fabric"

            artifact(tasks.shadowJar)
        }
    }
}