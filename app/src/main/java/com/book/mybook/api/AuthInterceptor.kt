package com.book.mybook.api

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Récupérer le token d'accès (cela se fait de manière synchrone car l'interceptor OkHttp est synchrone)
        val token = runBlocking { SessionManager.getAccessToken() }

        // Si un token existe, l'ajouter à l'en-tête Authorization
        return if (!token.isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(newRequest)
        } else {
            // Sinon, continuer avec la requête originale sans modification
            chain.proceed(originalRequest)
        }
    }
}