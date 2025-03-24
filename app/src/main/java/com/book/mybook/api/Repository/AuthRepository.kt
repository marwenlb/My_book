package com.book.mybook.api.Repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.book.mybook.api.Retrofit.RetrofitClient
import com.book.mybook.api.Service.AuthResponse
import com.book.mybook.api.Service.LoginRequest
import com.book.mybook.api.Service.RegisterRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepository(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_ID = stringPreferencesKey("user_id")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_ID]
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[USER_ID] = userId
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(USER_ID)
        }
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.authApiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    saveTokens(authResponse.accessToken, authResponse.refreshToken, authResponse.userId)
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Login failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, password: String, birthDate: String): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.authApiService.register(
                RegisterRequest(username, email, password, birthDate)
            )
            if (response.isSuccessful) {
                response.body()?.let { authResponse ->
                    saveTokens(authResponse.accessToken, authResponse.refreshToken, authResponse.userId)
                    Result.success(authResponse)
                } ?: Result.failure(Exception("Empty response body"))
            } else {
                Result.failure(Exception("Registration failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
