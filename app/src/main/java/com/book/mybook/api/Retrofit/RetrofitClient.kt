package com.book.mybook.api.Retrofit

import com.book.mybook.api.AuthInterceptor
import com.book.mybook.api.Service.AuthApiService
import com.book.mybook.api.Service.BookApiService
import com.book.mybook.api.Service.CollectionApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://gestionbibliotheque-production-2e39.up.railway.app"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Création du client OkHttp avec l'intercepteur d'authentification
    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor()) // Ajout de notre intercepteur d'authentification
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Création de l'instance Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Création des services API
    val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)
    val bookApiService: BookApiService = retrofit.create(BookApiService::class.java)
    val collectionApiService: CollectionApiService = retrofit.create(CollectionApiService::class.java)
}
