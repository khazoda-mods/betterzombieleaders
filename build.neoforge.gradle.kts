plugins {
    id("net.neoforged.moddev") version "2.0.141"
    id("neoforge-mutex")
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.slug") as String}-neoforge"

neoForge {
    version = property("deps.neo_loader") as String

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            ideName = "NC - ${sc.current.version}"
            gameDirectory = file("../../run/")
            client()

            taskBefore(tasks.named("prepareClientRun"))
        }

        register("server") {
            ideName = "NS - ${sc.current.version}"
            gameDirectory = file("../../run/")
            server()

            taskBefore(tasks.named("prepareServerRun"))
        }
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", "mod.id")
            register("name", "mod.name")
            register("version", "mod.version")
            register("minecraft", "mod.mc_compat")
            register("description", "mod.description")
            register("authors", "mod.authors")
            register("license", "mod.license")
            register("homepage", "mod.homepage")
            register("issues", "mod.issues")
            register("neoforge_loader", "deps.neoforge_loader")
        }

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
        filesMatching("*.mixins.json") { expand("java" to "JAVA_25") }
        exclude("fabric.mod.json")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds this NeoForge version and collects its release jars"

        inputs.property("version", project.property("mod.version"))
        from(jar.flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
    }
}
