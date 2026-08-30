plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
}

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = "${property("mod.slug") as String}-fabric"

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    implementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runs.configureEach {
        displayName.set("${if (name == "client") "FC" else "FS"} - ${sc.current.version}")
        appendProjectPathToDisplayName.set(false)
        preferGradleTask.set(true)
        generateRunConfig.set(true)
        runDirectory.set(rootProject.layout.projectDirectory.dir("run"))
        jvmArguments.add("-Dmixin.debug.export=true")
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
            register("sources", "mod.sources")
            register("issues", "mod.issues")
            register("discord", "mod.discord")
            register("fabric_loader", "deps.fabric_loader")
        }

        filesMatching("fabric.mod.json") { expand(props) }
        filesMatching("*.mixins.json") { expand("java" to "JAVA_25") }
        exclude("META-INF/neoforge.mods.toml")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds this Fabric version and collects its release jars"

        inputs.property("version", project.property("mod.version"))
        from(named<Jar>("jar").flatMap { it.archiveFile }, named<Jar>("sourcesJar").flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/${project.property("mod.version")}"))
    }
}
