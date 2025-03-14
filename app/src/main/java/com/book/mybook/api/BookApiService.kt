package com.book.mybook.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

data class BookRequest(
    val isbn: String,
    val rating: Int,
    val location: String,
    val status: String
)

data class ApiResponse(val message: String)

interface BookApiService {
    @GET("api/books/search")
    suspend fun searchBooks(
        @Header("Authorization") token: String,
        @Query("isbn") isbn: String
    ): ApiResponse
}

