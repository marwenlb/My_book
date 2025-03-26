package com.book.mybook.api

import android.annotation.SuppressLint
import android.content.Context
import com.book.mybook.api.Repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SessionManager {
    @SuppressLint("StaticFieldLeak")
    private var authRepository: AuthRepository? = null
    private var initialized = false

    // MutableStateFlow to track logout events
    private val _isLoggedOut = MutableStateFlow(false)
    val isLoggedOut: StateFlow<Boolean> = _isLoggedOut.asStateFlow()

    fun init(context: Context) {
        if (!initialized) {
            authRepository = AuthRepository(context)
            initialized = true
        }
    }

    suspend fun isLoggedIn(): Boolean {
        val token = authRepository?.accessToken?.first()
        return !token.isNullOrBlank()
    }

    fun logout(onComplete: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            authRepository?.clearTokens()

            // Notify observers that the user is logged out
            _isLoggedOut.value = true

            withContext(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    suspend fun getAccessToken(): String? {
        return authRepository?.accessToken?.first()
    }

    suspend fun getUserId(): String? {
        return authRepository?.userId?.first()
    }
}
