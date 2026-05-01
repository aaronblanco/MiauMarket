package convention

import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Apply Android library plugin only; other plugins (Kotlin, Hilt, KSP)
            // should be applied by the consuming module to avoid classpath/extension conflicts
            pluginManager.apply("com.android.library")

            extensions.configure<LibraryExtension> {
                compileSdk = 35

                // Leave defaultConfig to the consuming module to avoid API mismatches

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }

                buildFeatures {
                    buildConfig = true
                }
            }
        }
    }
}




