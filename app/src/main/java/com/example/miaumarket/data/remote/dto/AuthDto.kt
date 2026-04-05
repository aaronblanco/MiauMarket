package com.example.miaumarket.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "firstName") val firstName: String,
    @Json(name = "lastName") val lastName: String,
    @Json(name = "birthDate") val birthDate: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "role") val role: String = "user"
)

@JsonClass(generateAdapter = true)
data class UserResponse(
    @Json(name = "id") val id: String,
    @Json(name = "firstName") val firstName: String? = null,
    @Json(name = "lastName") val lastName: String? = null,
    @Json(name = "email") val email: String
)
