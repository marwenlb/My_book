package com.book.mybook.api.Service

import com.book.mybook.api.Model.CollectionItem
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectionApiService {
    @GET("/api/collections/users/{userId}")
    suspend fun getUserCollections(@Header("Authorization") token: String,
                                   @Path("userId") userId: String): List<CollectionItem>

    @POST("/api/collections")

    suspend fun createCollection(@Header("Authorization") token: String,
                                 @Body collection: CollectionItem)
}
