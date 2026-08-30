pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9.7"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) {
            for (loader in loaders) {
                version("$version-$loader", version).buildscript("build.$loader.gradle.kts")
            }
        }

        match("26.1", "fabric", "neoforge")
        match("26.2", "fabric", "neoforge")
        vcsVersion = "26.1-fabric"
    }
}

rootProject.name = "better-zombie-leaders"
