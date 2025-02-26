package com.book.mybook.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.book.mybook.model.RegisterResponse
import com.book.mybook.repository.AuthRepository
import com.book.mybook.util.SessionManager
import com.book.mybook.viewmodel.SignupViewModel

@Composable
fun SignupScreen(
    navController: NavController,
    viewModel: SignupViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }

    val validationState by viewModel.validationState.collectAsState()
    val signupState by viewModel.signupState.collectAsState()

    // Effect to handle registration result
    LaunchedEffect(signupState) {
        when (signupState) {
            is AuthRepository.Result.Success -> {
                val result = signupState as AuthRepository.Result.Success<RegisterResponse>
                sessionManager.saveTokens(result.data.accessToken, result.data.refreshToken)
                Toast.makeText(context, "Inscription réussie", Toast.LENGTH_SHORT).show()
                navController.navigate("mes_livres") {
                    // Clear back stack so user can't go back to signup screen
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthRepository.Result.Error -> {
                val errorMsg = (signupState as AuthRepository.Result.Error).message
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
            else -> {} // Loading or null state
        }
    }

    // Cleanup when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Créer un compte", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = validationState.emailError != null,
            supportingText = { validationState.emailError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom de l'utilisateur") },
            isError = validationState.usernameError != null,
            supportingText = { validationState.usernameError?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            label = { Text("Date de naissance (JJ/MM/AAAA)") },
            isError = validationState.birthDateError != null,
            supportingText = { validationState.birthDateError?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            isError = validationState.passwordError != null,
            supportingText = { validationState.passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
            )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirmer le mot de passe") },
            isError = validationState.confirmPasswordError != null,
            supportingText = { validationState.confirmPasswordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)

            )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.signup(email, username, password, confirmPassword, birthDate)
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = signupState !is AuthRepository.Result.Loading
        ) {
            if (signupState is AuthRepository.Result.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("S'inscrire")
            }
        }


        TextButton(onClick = { navController.popBackStack() }) {
            Text("Retour à la connexion")
        }
    }
}