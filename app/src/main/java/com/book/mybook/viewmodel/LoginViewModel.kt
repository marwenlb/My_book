package com.book.mybook.viewmodel

 import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import com.book.mybook.model.LoginResponse
 import com.book.mybook.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _loginState = MutableStateFlow<AuthRepository.Result<LoginResponse>?>(null)
    val loginState: StateFlow<AuthRepository.Result<LoginResponse>?> = _loginState

    private val _validationState = MutableStateFlow<ValidationState>(ValidationState())
    val validationState: StateFlow<ValidationState> = _validationState

    data class ValidationState(
        val usernameError: String? = null,
        val passwordError: String? = null
    )

    fun login(username: String, password: String) {
        if (!validateInputs(username, password)) {
            return
        }

        _loginState.value = AuthRepository.Result.Loading

        viewModelScope.launch {
            val result = authRepository.login(username, password)
            _loginState.value = result
        }
    }

    private fun validateInputs(username: String, password: String): Boolean {
        val validationState = ValidationState(
            usernameError = validateUsername(username),
            passwordError = validatePassword(password)
        )

        _validationState.value = validationState

        return validationState.usernameError == null && validationState.passwordError == null
    }

    private fun validateUsername(username: String): String? {
        if (username.isBlank()) {
            return "Le nom d'utilisateur ne peut pas être vide"
        }
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) {
            return "Le mot de passe ne peut pas être vide"
        }
        return null
    }

    fun resetState() {
        _loginState.value = null
        _validationState.value = ValidationState()
    }

}