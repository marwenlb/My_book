package com.book.mybook.api.Service
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val birthDate: String
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/api/auth/refresh-token")
    suspend fun refreshToken(): Response<AuthResponse>
}