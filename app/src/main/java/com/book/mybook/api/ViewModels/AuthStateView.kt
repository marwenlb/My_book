package com.book.mybook.api.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.book.mybook.api.Repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    private val _loginState = MutableStateFlow<AuthState>(AuthState.Initial)
    val loginState: StateFlow<AuthState> = _loginState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            repository.login(username, password)
                .onSuccess {
                    _loginState.value = AuthState.Success
                }
                .onFailure { error ->
                    _loginState.value = AuthState.Error(error.message ?: "Une erreur s'est produite")
                }
        }
    }
}

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(application)

    private val _signupState = MutableStateFlow<AuthState>(AuthState.Initial)
    val signupState: StateFlow<AuthState> = _signupState.asStateFlow()

    fun signup(username: String, email: String, password: String) {
        viewModelScope.launch {
            _signupState.value = AuthState.Loading
            // Format today's date for birthDate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val birthDate = dateFormat.format(Date())

            repository.register(username, email, password, birthDate)
                .onSuccess {
                    _signupState.value = AuthState.Success
                }
                .onFailure { error ->
                    _signupState.value = AuthState.Error(error.message ?: "Une erreur s'est produite")
                }
        }
    }
}