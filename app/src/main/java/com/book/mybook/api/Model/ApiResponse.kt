package com.book.mybook.api.Model

sealed class ApiResponse {
    data class Success(val message: String) : ApiResponse()
    data class Failure(val error: String) : ApiResponse()
}
