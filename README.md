## Installation

FlowerUI is distributed through the **JitPack** repository.

FlowerUI is split into two artifacts:

* **FlowerUI-common** — The platform-independent UI framework containing widgets, layouts, themes, animations, and rendering abstractions.
* **FlowerUI-Fabric** — The Fabric-specific implementation providing Minecraft integration, rendering hooks, and platform features.

---

### Adding the Repository

Configure your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

---

## Common Module

Add FlowerUI-common to your shared/common module:

### Kotlin DSL

```kotlin
dependencies {
    implementation("com.github.starletboh:FlowerUI-common:1.0.0")
}
```

### Groovy

```groovy
dependencies {
    implementation 'com.github.starletboh:FlowerUI-common:1.0.0'
}
```

---

## Fabric Module

Add the matching FlowerUI-Fabric version to your Minecraft/Fabric module.

Example:

```kotlin
dependencies {
    modImplementation("com.github.starletboh:FlowerUI-Fabric:1.0.0-mc1.21.9")
}
```

For Minecraft 26.1:

```kotlin
dependencies {
    modImplementation("com.github.starletboh:FlowerUI-Fabric:1.0.0-mc26.1")
}
```

The Fabric artifact provides:

* Minecraft screen integration
* Fabric event handling
* Platform-specific rendering
* Loader integration

---

## Requirements

* **Java:** 21
* **Minecraft:** 1.21.x
* **Fabric Loader**
* **Fabric API**
* **Fabric Language Kotlin**

---

## Implementation Example
