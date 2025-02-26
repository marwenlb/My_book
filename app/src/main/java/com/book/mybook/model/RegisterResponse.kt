package com.book.mybook.model

data class RegisterResponse(
    val accessToken: String,
    val refreshToken: String
)