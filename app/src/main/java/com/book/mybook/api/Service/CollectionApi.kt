package com.book.mybook.api.Service

import com.book.mybook.api.Model.CollectionItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


data class ShareCollectionRequest(
    val collectionId: Long,
    val permissions: List<String>
)

data class ShareCollectionResponse(
    val url: String
)


interface CollectionApiService {
    @GET("/api/collections/users/{userId}")
    suspend fun getUserCollections(@Header("Authorization") token: String,
                                   @Path("userId") userId: String): List<CollectionItem>

    @POST("/api/collections")

    suspend fun createCollection(@Header("Authorization") token: String,
                                 @Body collection: CollectionItem)

    // CollectionApiService.kt - Ajouter cet endpoint
    @GET("/api/collections/{collectionId}")
    suspend fun getCollectionById(@Header("Authorization") token: String,
                                  @Path("collectionId") collectionId: String): CollectionItem

    @POST("/api/collections/share")
    suspend fun shareCollection(
        @Header("Authorization") token: String,
        @Body request: ShareCollectionRequest
    ): ShareCollectionResponse
}
