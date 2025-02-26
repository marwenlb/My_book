package com.book.mybook.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)