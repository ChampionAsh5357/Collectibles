import net.minecraftforge.gradle.common.util.RunConfig
import java.time.Instant
import java.time.format.DateTimeFormatter

// Attach plugins
plugins {
    `java-library`
    alias(libs.plugins.forge.gradle)
    alias(libs.plugins.parchment.librarian)
//    alias(libs.plugins.sponge.mixin) TODO: Check if actually needed
    alias(libs.plugins.licenser)
}

// Get variables
internal val minecraftVersion: String = libs.versions.minecraft.get()
internal val mappingsVersion: String = libs.versions.parchment.mappings.get()
internal val forgeVersion: String = libs.versions.forge.main.get().split("-")[1]
internal val authorName: String by extra
internal val authorGroup: String by extra
internal val mappingsType: String by extra
internal val modId: String by extra
internal val modSpecVersion: String by extra
internal val modImplVersion: String by extra

internal val modName: String by extra
internal val modDesc: String by extra
internal val license: String by extra
internal val displayUrl: String by extra
internal val issueTrackerUrl: String by extra

internal val licenseHeader: TextResource = resources.text.fromFile(rootProject.file("LICENSE-HEADER"))

// Project Info
version = "${minecraftVersion}-${modSpecVersion}.${modImplVersion}"
group = authorGroup
base.archivesName.set(modId)

// Toolchain information
java.toolchain.languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))

// Create source sets
internal val generatedSourceSet: SourceSet = sourceSets.create("generated")

// Add sources to other sources
sourceSets.main {
    resources {
        source(generatedSourceSet.resources)
        exclude(".cache/")
    }
}

// Add dependencies
dependencies {
    minecraft(libs.forge)
}

// Setup runs
internal val runSetup: Map<String, (RunConfig) -> Unit> = mapOf(
    "client" to {},
    "server" to {},
    "gameTestServer" to {},
    "data" to { config ->
        // Setup datagen configurations
        config.args(
            "--mod", modId,
            "--all",
            "--output", generatedSourceSet.resources.srcDirs.first()
        )
        sourceSets["main"].resources.srcDirs.forEach { config.args("--existing", it) }
    }
)

minecraft {
    // Set mapping information
    mappings(mappingsType, "${mappingsVersion}-${minecraftVersion}")

    // Setup run configurations
    runSetup.forEach {
        runs.create(it.key) {
            // Delegate run directory
            workingDirectory(rootProject.file("run/${it.key}"))

            // Set modules within IDEA
            ideaModule = "${modId}.main"

            // Allow game tests to be run
            property("forge.enabledGameTestNamespaces", modId)

            // Add config-specific settings
            it.value(this)

            // Add main source set to mod environment
            mods.create(modId).source(sourceSets["main"])
        }
    }
}

// Set license information
license {
    header.set(licenseHeader)

    properties {
        set("author", authorName)
        set("license", license)
    }

    include("**/*.java")
}

// Basic mod properties to copy to mods.toml
internal val modProperties: Map<String, String> = mapOf(
    "loader_version" to forgeVersion.split(".")[0],
    "license" to license,
    "issue_tracker_url" to issueTrackerUrl,
    "mod_id" to modId,
    "version" to project.version.toString(),
    "mod_name" to modName,
    "display_url" to displayUrl,
    "author" to authorName,
    "mod_description" to modDesc,
    "minecraft_version" to minecraftVersion,
    "forge_version" to forgeVersion
)

// Apply properties to resources
tasks.processResources {
    // Copy over data to mods.toml file
    outputs.upToDateWhen { false }
    inputs.properties(modProperties)

    filesMatching("**/mods.toml") {
        expand(modProperties)
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Set jar properties
tasks.jar {
    finalizedBy("reobfJar")

    manifest.attributes(mapOf(
        "Specification-Title" to modName,
        "Specification-Vendor" to authorName,
        "Specification-Version" to modSpecVersion,
        "Implementation-Title" to modName,
        "Implementation-Vendor" to authorName,
        "Implementation-Version" to project.version,
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    ))
}
