package com.book.mybook.repository

import ApiService
import com.book.mybook.model.LoginRequest
import com.book.mybook.model.LoginResponse
import com.book.mybook.model.RegisterRequest
import com.book.mybook.model.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }

    suspend fun register(username: String, email: String, password: String, birthDate: String): Result<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(username, email, password, birthDate)
                val response = apiService.register(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Response body is empty")
                } else {
                    Result.Error("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Registration failed: ${e.message}")
            }
        }
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(username, password)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Response body is empty")
                } else {
                    Result.Error("Login failed: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Login failed: ${e.message}")
            }
        }
    }

}