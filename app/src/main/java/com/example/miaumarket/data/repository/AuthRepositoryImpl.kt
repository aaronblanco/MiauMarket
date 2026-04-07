package com.example.miaumarket.data.repository

import com.example.miaumarket.data.local.SessionManager
import com.example.miaumarket.data.remote.AuthApi
import com.example.miaumarket.data.remote.dto.LoginRequest
import com.example.miaumarket.data.remote.dto.RegisterRequest
import com.example.miaumarket.data.remote.dto.UserResponse
import com.example.miaumarket.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<String> {
        return try {
            val response = authApi.login(request)
            sessionManager.saveToken(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<String> {
        return try {
            val response = authApi.register(request)
            sessionManager.saveToken(response.token)
            Result.success(response.token)
        } catch (e: Exception) {
            Result.failure(IllegalStateException("No se ha podido completar el registro", e))
        }
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } catch (_: Exception) {
            // Log error or handle failure if necessary, but proceed to clear token locally
        } finally {
            sessionManager.clearToken()
        }
    }

    override fun getAuthToken(): Flow<String?> {
        return sessionManager.token
    }

    override suspend fun getCurrentUser(): Result<UserResponse> {
        return try {
            val response = authApi.getMe()
            Result.success(response.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
