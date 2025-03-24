package com.book.mybook.viewmodel

import BookModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.book.mybook.api.Repository.BookRepository
import com.book.mybook.api.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookDetailViewModel : ViewModel() {
    private val repository = BookRepository()

    private val _book = MutableStateFlow<BookModel?>(null)
    val book: StateFlow<BookModel?> = _book.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadBook(bookId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = SessionManager.getAccessToken()
            if (token.isNullOrBlank()) {
                _error.value = "Authentication token is missing"
                _isLoading.value = false
                return@launch
            }

            repository.getBookById(bookId, token).fold(
                onSuccess = { result ->
                    _book.value = result
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Une erreur s'est produite"
                    _isLoading.value = false
                }
            )
        }
    }

    fun clearError() {
        _error.value = null
    }
}