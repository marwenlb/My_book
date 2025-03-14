package com.book.mybook.api.Model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val birthDate: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
