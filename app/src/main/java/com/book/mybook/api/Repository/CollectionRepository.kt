package com.book.mybook.api.Repository

import com.book.mybook.api.Model.CollectionItem
import com.book.mybook.api.Retrofit.RetrofitClient
import com.book.mybook.api.Service.ShareCollectionRequest

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

    suspend fun getPublicCollections(token: String): Result<List<CollectionItem>> {
        return try {
            val collections = api.getPublicCollections("Bearer $token")
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

    suspend fun getCollectionById(collectionId: String, token: String): Result<CollectionItem> {
        return try {
            val collection = api.getCollectionById("Bearer $token", collectionId)
            Result.success(collection)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun shareCollection(collectionId: Long, token: String, permissions: List<String>): Result<String> {
        return try {
            val request = ShareCollectionRequest(collectionId = collectionId, permissions = permissions)
            val response = api.shareCollection("Bearer $token", request)
            Result.success(response.url)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}