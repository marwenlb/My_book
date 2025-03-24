package com.book.mybook.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.book.mybook.api.Model.CollectionItem
import com.book.mybook.api.Repository.CollectionRepository
import com.book.mybook.api.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionViewModel : ViewModel() {
    private val repository = CollectionRepository()

    private val _collections = MutableStateFlow<List<CollectionItem>>(emptyList())
    val collections: StateFlow<List<CollectionItem>> = _collections.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val collectionName = mutableStateOf("")
    val collectionDescription = mutableStateOf("")
    val isPublic = mutableStateOf(true)

    fun loadUserCollections(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = SessionManager.getAccessToken()
            if (token.isNullOrBlank()) {
                _error.value = "Authentication token is missing"
                _isLoading.value = false
                return@launch
            }

            repository.getUserCollections(userId, token).fold(
                onSuccess = { result ->
                    _collections.value = result
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Une erreur s'est produite"
                    _isLoading.value = false
                }
            )
        }
    }

    fun createCollection(userId: String, onSuccess: () -> Unit) {
        if (collectionName.value.isBlank()) {
            _error.value = "Le nom de la collection ne peut pas être vide"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = SessionManager.getAccessToken()
            if (token.isNullOrBlank()) {
                _error.value = "Authentication token is missing"
                _isLoading.value = false
                return@launch
            }

            val newCollection = CollectionItem(
                id = "",
                name = collectionName.value,
                description = collectionDescription.value,
                isPublic = isPublic.value
            )

            repository.createCollection(newCollection, token).fold(
                onSuccess = {
                    loadUserCollections(userId)
                    resetForm()
                    onSuccess()
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erreur lors de la création de la collection"
                    _isLoading.value = false
                }
            )
        }
    }
    private val _sharedUrl = MutableStateFlow<String?>(null)
    val sharedUrl: StateFlow<String?> = _sharedUrl.asStateFlow()

    fun shareCollection(collectionId: Long, permissions: List<String> = listOf("COLLECTION_READ", "COLLECTION_UPDATE",)) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val token = SessionManager.getAccessToken()
            if (token.isNullOrBlank()) {
                _error.value = "Authentication token is missing"
                _isLoading.value = false
                return@launch
            }

            repository.shareCollection(collectionId, token, permissions).fold(
                onSuccess = { url ->
                    _sharedUrl.value = url
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Erreur lors du partage de la collection"
                    _isLoading.value = false
                }
            )
        }
    }
    fun clearSharedUrl() {
        _sharedUrl.value = null
    }

    fun clearError() {
        _error.value = null
    }

    private fun resetForm() {
        collectionName.value = ""
        collectionDescription.value = ""
        isPublic.value = true
    }
}
