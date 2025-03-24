package com.book.mybook.api.Service

import BookModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// BookRequest should be used for sending a request body if needed
data class BookRequest(
    val isbn: String,
    val rating: Int,
    val location: String,
    val status: String
)

data class ApiResponse(val message: String)

interface BookApiService {
    // Search books by ISBN (already defined)
    @GET("api/books/search")
    suspend fun searchBooks(
        @Header("Authorization") token: String,
        @Query("isbn") isbn: String
    ): ApiResponse

    // Get a book by ID
    @GET("/api/books/{bookId}")
    suspend fun getBookById(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: String
    ): BookModel

    // Add a book to the collection by ISBN
    @POST("api/books/add")
    suspend fun addBookToCollection(
        @Header("Authorization") token: String,
        @Body bookRequest: BookRequest
    ): ApiResponse
}
