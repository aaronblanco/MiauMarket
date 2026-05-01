plugins {
    id("org.jetbrains.kotlin.jvm")
}

group = "com.example.miaumarket.core"
version = "0.1.0"

dependencies {
    // DTOs and domain interfaces only — keep minimal JVM deps
    implementation("com.squareup.moshi:moshi:1.15.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("com.squareup.retrofit2:retrofit:2.12.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.12.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("javax.inject:javax.inject:1")

    testImplementation("junit:junit:4.13.2")
}




















