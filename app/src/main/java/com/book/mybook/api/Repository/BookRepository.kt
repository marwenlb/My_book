package com.book.mybook.api.Repository

import BookModel
import android.util.Log
import com.book.mybook.api.Retrofit.RetrofitClient
import com.book.mybook.api.Service.ApiResponse

class BookRepository {
    private val api = RetrofitClient.bookApiService

    suspend fun addBookToCollection(token: String, isbn: String): ApiResponse? {
        return try {
            val response = api.searchBooks("Bearer $token", isbn)
            Log.d("BookAPI", "Réponse reçue: ${response.message}")
            response
        } catch (e: Exception) {
            Log.e("BookAPI", "Erreur lors de la recherche du livre", e)
            null
        }
    }
    suspend fun getBookById(bookId: String, token: String): Result<BookModel> {
        return try {
            val book = api.getBookById("Bearer $token", bookId)
            Result.success(book)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
suspend fun addBookToCollection(token: String, isbn: String): ApiResponse? {
    return try {
        val response = RetrofitClient.bookApiService.searchBooks("Bearer $token", isbn)
        Log.d("BookAPI", "Réponse reçue: ${response.message}")
        response
    } catch (e: Exception) {
        Log.e("BookAPI", "Erreur lors de la recherche du livre", e)
        null
    }
}