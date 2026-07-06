plugins {
    id("net.fabricmc.fabric-loom-remap")
    id("maven-publish")
    kotlin("jvm")
    id("com.gradleup.shadow") version "8.3.0"
}

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

    // Jackson (jackson 3.x groupId changed to tools.jackson)
    val jackson = "3.1.3"
    implementation("tools.jackson.core:jackson-databind:$jackson")
    include("tools.jackson.core:jackson-databind:$jackson")
    implementation("tools.jackson.core:jackson-core:${jackson}")
    include("tools.jackson.core:jackson-core:${jackson}")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
//    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")
//    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.22")
    include("com.fasterxml.jackson.core:jackson-annotations:2.22")
//    include("com.fasterxml.jackson.core:jackson-annotations:2.18.2")
//    include("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")
    implementation("com.formdev:svgSalamander:1.1.4")
    include("com.formdev:svgSalamander:1.1.4")
//    implementation("org.apache-extras.beanshell:bsh:2.0b6")
//    include("org.apache-extras.beanshell:bsh:2.0b6")


}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.shadowJar {
    archiveClassifier.set("dev-shadow")
    configurations = emptyList()
    dependencies {
        include(project(":common"))
        include(dependency("tools.jackson.core:jackson-databind"))
        include(dependency("tools.jackson.core:jackson-core"))        // ADD THIS
        include(dependency("com.fasterxml.jackson.core:jackson-annotations")) // ADD THIS
        include(dependency("com.formdev:svgSalamander"))

    }
    from(project(":common").sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.remapJar {
    dependsOn(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.get().archiveFile)
    archiveClassifier.set("")
}
