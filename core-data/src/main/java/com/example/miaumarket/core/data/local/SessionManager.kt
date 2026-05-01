package com.example.miaumarket.core.data.local

import kotlinx.coroutines.flow.Flow

/**
 * SessionManager interface for core-data. The Android implementation that uses
 * DataStore and Context must live in the `app` module. This interface allows
 * the app and wear modules to depend on the core-data types without pulling
 * Android-only dependencies into the core-data JVM module.
 */
interface SessionManager {
    val token: Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}


