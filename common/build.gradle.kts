plugins {
    kotlin("jvm")
    java
}

group = "me.starletboh.flowerui"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    implementation("com.formdev:svgSalamander:1.1.4")
    implementation("tools.jackson.core:jackson-databind:3.1.3")
    implementation("tools.jackson.core:jackson-core:3.1.3")
//    implementation("org.apache-extras.beanshell:bsh:2.0b6")
//    implementation("net.sourceforge.cssparser:cssparser:0.9.30")
//    implementation("org.w3c.css:sac:1.3")
//    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")

}
