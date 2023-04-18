// Add gradle plugin repositories
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "Minecraft Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
        maven {
            name = "Parchment MC"
            url = uri("https://maven.parchmentmc.org/")
        }
//        maven {
//            name = "SpongePowered"
//            url = uri("https://repo.spongepowered.org/repository/maven-public/")
//        }
    }

    // Handle resolutions for plugins block
//    resolutionStrategy.eachPlugin {
//        if (requested.id.id == "org.spongepowered.mixin")
//            useModule(mapOf(
//                "group" to "org.spongepowered",
//                "name" to "mixingradle",
//                "version" to requested.version
//            ))
//    }
}

// Set project name
internal val modId: String by extra
rootProject.name = modId
