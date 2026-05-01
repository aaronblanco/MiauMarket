pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "9.1.1"
        id("com.android.library") version "9.1.1"
        id("org.jetbrains.kotlin.android") version "2.2.10"
        id("org.jetbrains.kotlin.plugin.serialization") version "2.2.10"
        id("com.google.dagger.hilt.android") version "2.59.2"
        id("com.google.devtools.ksp") version "2.3.2"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MiauMarket"
include(":app")
include(":core-data")
// wear module temporarily excluded to allow JVM build while migrating core-data
// include(":wear")
includeBuild("build-logic")
