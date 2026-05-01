package com.example.miaumarket.core.data.remote

import com.example.miaumarket.core.data.local.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * AuthInterceptor depends only on the `SessionManager` interface defined in
 * core-data. The actual Android implementation will be provided by the app
 * module and injected via Hilt there. Keep this class platform-neutral.
 */
class AuthInterceptor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            sessionManager.token.first()
        }
        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()
        return chain.proceed(request)
    }
}


