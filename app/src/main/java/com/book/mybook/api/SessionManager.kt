package com.book.mybook.api

import android.annotation.SuppressLint
import android.content.Context
import com.book.mybook.api.Repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object SessionManager {
    @SuppressLint("StaticFieldLeak")
    private var authRepository: AuthRepository? = null
    private var initialized = false

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
