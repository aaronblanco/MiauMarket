plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("libraryPlugin") {
            id = "miaumarket.library"
            implementationClass = "convention.LibraryConventionPlugin"
        }
    }
}

dependencies {
    implementation("com.android.tools.build:gradle:9.1.1")
}


