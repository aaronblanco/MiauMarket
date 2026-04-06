package com.example.miaumarket.domain.repository

import com.example.miaumarket.data.remote.dto.LoginRequest
import com.example.miaumarket.data.remote.dto.RegisterRequest
import com.example.miaumarket.data.remote.dto.UserResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<String>
    suspend fun register(request: RegisterRequest): Result<Unit>
    suspend fun logout()
    fun getAuthToken(): Flow<String?>
    suspend fun getCurrentUser(): Result<UserResponse>
}
