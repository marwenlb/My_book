package com.book.mybook.api.Model

import BookModel

data class CollectionItem(
    val id: String,
    val name: String,
    val description: String,
    val createdAt: String? = null,
    val user: UserModel? = null,
    val books: List<BookModel> = emptyList(),
    val isShareable: Boolean
)

data class UserModel(
    val id: Int,
    val username: String
)
