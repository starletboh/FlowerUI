plugins {
    kotlin("jvm")
    java
    `maven-publish`
}

group = "me.starletboh.flowerui"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
}

dependencies {
    implementation("com.formdev:svgSalamander:1.1.4")
    implementation("tools.jackson.core:jackson-databind:3.1.3")
    implementation("tools.jackson.core:jackson-core:3.1.3")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            artifactId = "FlowerUI-common"
        }
    }
}