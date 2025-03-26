package com.book.mybook.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Retrieve token synchronously (since OkHttp interceptors are synchronous)
        val token = runBlocking { SessionManager.getAccessToken() }

        val requestWithToken = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        val response = chain.proceed(requestWithToken)

        if (response.code == 403 || response.code == 401) {
            runBlocking {
                SessionManager.logout()
            }
        }

        return response
    }
}
