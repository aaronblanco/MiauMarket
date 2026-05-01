package com.example.miaumarket.core.data.repository

import com.example.miaumarket.core.data.local.SessionManager
import com.example.miaumarket.core.data.remote.AuthApi
import com.example.miaumarket.core.data.remote.dto.LoginRequest
import com.example.miaumarket.core.data.remote.dto.RegisterRequest
import com.example.miaumarket.core.data.remote.dto.UserResponse
import com.example.miaumarket.core.data.domain.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido iniciar sesión"))
        }
    }

    override suspend fun register(request: RegisterRequest): Result<String> {
        return try {
            val response = authApi.register(request)
            sessionManager.saveToken(response.token)
            Result.success(response.token)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido completar el registro"))
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
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e.toUserFacingException("No se ha podido obtener el usuario actual"))
        }
    }

    private fun Throwable.toUserFacingException(defaultMessage: String): Throwable {
        val message = when (this) {
            is HttpException -> {
                response()?.errorBody()?.string()
                    ?.extractBackendMessage()
                    ?: when (code()) {
                        401 -> "Credenciales incorrectas o sesión caducada"
                        403 -> "No tienes permisos para realizar esta acción"
                        404 -> "No se ha encontrado el recurso solicitado"
                        in 500..599 -> "El servidor no está disponible en este momento"
                        else -> defaultMessage
                    }
            }
            is IOException -> "No se ha podido conectar con el servidor"
            else -> message?.takeIf { it.isNotBlank() } ?: defaultMessage
        }

        return IllegalStateException(message, this)
    }

    private fun String.extractBackendMessage(): String? {
        val trimmed = trim()
        if (trimmed.isEmpty()) return null

        Regex("\"message\"\\s*:\\s*\"([^\"]+)\"").find(trimmed)?.groupValues?.getOrNull(1)?.let { return it }
        Regex("\"error\"\\s*:\\s*\"([^\"]+)\"").find(trimmed)?.groupValues?.getOrNull(1)?.let { return it }

        return trimmed.removePrefix("{").removeSuffix("}").takeIf { it.isNotBlank() }
    }
}

