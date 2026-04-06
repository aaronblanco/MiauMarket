package com.example.miaumarket.data.remote

import com.example.miaumarket.data.remote.dto.LoginRequest
import com.example.miaumarket.data.remote.dto.LoginResponse
import com.example.miaumarket.data.remote.dto.RegisterRequest
import com.example.miaumarket.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Response

interface AuthApi {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<Unit>

    @GET("api/auth/me")
    suspend fun getMe(): UserResponse

    @POST("api/auth/logout")
    suspend fun logout()
}
