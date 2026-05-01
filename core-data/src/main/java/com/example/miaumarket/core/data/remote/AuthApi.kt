package com.example.miaumarket.core.data.remote

import com.example.miaumarket.core.data.remote.dto.LoginRequest
import com.example.miaumarket.core.data.remote.dto.AuthResponse
import com.example.miaumarket.core.data.remote.dto.MeResponse
import com.example.miaumarket.core.data.remote.dto.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("api/auth/me")
    suspend fun getMe(): MeResponse

    @POST("api/auth/logout")
    suspend fun logout()
}

