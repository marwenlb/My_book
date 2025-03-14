package com.book.mybook.api

import android.util.Log

suspend fun addBookToCollection(token: String, isbn: String): ApiResponse? {
    return try {
        val response = RetrofitInstance.api.searchBooks("Bearer $token", isbn)
        Log.d("BookAPI", "Réponse reçue: ${response.message}")
        response
    } catch (e: Exception) {
        Log.e("BookAPI", "Erreur lors de la recherche du livre", e)
        null
    }
}
