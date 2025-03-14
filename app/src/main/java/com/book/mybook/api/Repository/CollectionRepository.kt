package com.book.mybook.api.Repository

import com.book.mybook.api.Model.CollectionItem
import com.book.mybook.api.Retrofit.RetrofitClient

class CollectionRepository {
    private val api = RetrofitClient.collectionApiService

    suspend fun getUserCollections(userId: String, token: String): Result<List<CollectionItem>> {
        return try {
            val collections = api.getUserCollections("Bearer $token", userId)
            Result.success(collections)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCollection(collection: CollectionItem, token: String): Result<Unit> {
        return try {
            api.createCollection("Bearer $token", collection)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
