package com.book.mybook.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val birthDate: String
)