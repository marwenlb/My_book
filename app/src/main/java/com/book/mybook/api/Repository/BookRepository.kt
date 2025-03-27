package com.book.mybook.api.Repository

import BookModel
import android.util.Log
import com.book.mybook.api.Retrofit.RetrofitClient
import com.book.mybook.api.Service.ApiResponse
import com.book.mybook.api.Service.BookApiService

class BookRepository {
    private val api = RetrofitClient.bookApiService
    suspend fun getBookById(bookId: String, token: String): Result<BookModel> {
        return try {
            val book = api.getBookById("Bearer $token", bookId)
            Result.success(book)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}

suspend fun addBookToDB(token: String, isbn: String): BookModel? {
    return try {
        // Utiliser une liste de BookModel pour capturer un tableau JSON
        val books = RetrofitClient.bookApiService.searchBookByIsbn("Bearer $token", isbn)
        // Prendre le premier livre de la liste s'il existe
        books.firstOrNull()
    } catch (e: Exception) {
        Log.e("BookAPI", "Erreur lors de la recherche du livre", e)
        null
    }
}



suspend fun addBookToCollection(token: String, collectionId: Int, bookId: Int): ApiResponse? {
    return try {
        // Créer l'objet de requête
        val addBookRequest = BookApiService.AddBookRequest(collectionId, bookId)

        // Appeler l'API sans analyser la réponse
        val response = RetrofitClient.bookApiService.addBookToCollection("Bearer $token", addBookRequest)

        // Retourner la réponse complète (pas seulement le body)
        response
    } catch (e: Exception) {
        Log.e("BookAPI", "Error adding book to collection", e)
        null
    }
}