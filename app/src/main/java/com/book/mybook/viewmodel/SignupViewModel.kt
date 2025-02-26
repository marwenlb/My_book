package com.book.mybook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.book.mybook.model.RegisterResponse
import com.book.mybook.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class SignupViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _signupState = MutableStateFlow<AuthRepository.Result<RegisterResponse>?>(null)
    val signupState: StateFlow<AuthRepository.Result<RegisterResponse>?> = _signupState

    private val _validationState = MutableStateFlow<ValidationState>(ValidationState())
    val validationState: StateFlow<ValidationState> = _validationState

    data class ValidationState(
        val emailError: String? = null,
        val usernameError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val birthDateError: String? = null
    )

    fun signup(email: String, username: String, password: String, confirmPassword: String, birthDate: String) {
        if (!validateInputs(email, username, password, confirmPassword, birthDate)) {
            return
        }

        // Format the birthdate from DD/MM/YYYY to YYYY-MM-DD format
        val formattedBirthDate = formatBirthDate(birthDate)

        _signupState.value = AuthRepository.Result.Loading

        viewModelScope.launch {
            val result = authRepository.register(username, email, password, formattedBirthDate)
            _signupState.value = result
        }
    }

    private fun formatBirthDate(birthDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(birthDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            birthDate // Return original if parsing fails
        }
    }

    private fun validateInputs(email: String, username: String, password: String, confirmPassword: String, birthDate: String): Boolean {
        val validationState = ValidationState(
            emailError = validateEmail(email),
            usernameError = validateUsername(username),
            passwordError = validatePassword(password),
            confirmPasswordError = validateConfirmPassword(password, confirmPassword),
            birthDateError = validateBirthDate(birthDate)
        )

        _validationState.value = validationState

        return validationState.emailError == null &&
                validationState.usernameError == null &&
                validationState.passwordError == null &&
                validationState.confirmPasswordError == null &&
                validationState.birthDateError == null
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) {
            return "L'email ne peut pas être vide"
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Format d'email invalide"
        }
        return null
    }

    private fun validateUsername(username: String): String? {
        if (username.isBlank()) {
            return "Le nom d'utilisateur ne peut pas être vide"
        }
        if (username.length < 3) {
            return "Le nom d'utilisateur doit contenir au moins 3 caractères"
        }
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) {
            return "Le mot de passe ne peut pas être vide"
        }
        if (password.length < 6) {
            return "Le mot de passe doit contenir au moins 6 caractères"
        }
        return null
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (confirmPassword.isBlank()) {
            return "Veuillez confirmer votre mot de passe"
        }
        if (password != confirmPassword) {
            return "Les mots de passe ne correspondent pas"
        }
        return null
    }

    private fun validateBirthDate(birthDate: String): String? {
        if (birthDate.isBlank()) {
            return "La date de naissance ne peut pas être vide"
        }

        val datePattern = Regex("^\\d{2}/\\d{2}/\\d{4}$")
        if (!datePattern.matches(birthDate)) {
            return "Format de date invalide (JJ/MM/AAAA)"
        }

        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(birthDate)
            null // Valid date
        } catch (e: Exception) {
            "Date invalide"
        }
    }

    fun resetState() {
        _signupState.value = null
        _validationState.value = ValidationState()
    }
}
