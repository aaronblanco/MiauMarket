package com.example.miaumarket.core.data.di

/**
 * Placeholder NetworkModule for core-data JVM module.
 * The real Android/Hilt NetworkModule lives in the `app` module and provides
 * Retrofit/OkHttp/Hilt bindings. This file only keeps a small constant to
 * document the default BASE_URL used by the project.
 */
object NetworkModule {
    const val BASE_URL = "http://10.0.2.2:3000/"
}


