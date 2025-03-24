// CollectionDetailViewModel.kt
package com.book.mybook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.book.mybook.api.Model.CollectionItem
import com.book.mybook.api.Repository.CollectionRepository
import com.book.mybook.api.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionDetailViewModel : ViewModel() {
    private val repository = CollectionRepository()

    private val _collection = MutableStateFlow<CollectionItem?>(null)
    val collection: StateFlow<CollectionItem?> = _collection.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadCollection(collectionId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = SessionManager.getAccessToken()
            if (token.isNullOrBlank()) {
                _error.value = "Authentication token is missing"
                _isLoading.value = false
                return@launch
            }

            repository.getCollectionById(collectionId, token).fold(
                onSuccess = { result ->
                    _collection.value = result
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