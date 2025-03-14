package com.book.mybook.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.book.mybook.api.ViewModels.AuthState
import com.book.mybook.api.ViewModels.SignupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val signupViewModel: SignupViewModel = viewModel()
    val authState by signupViewModel.signupState.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                errorMessage = (authState as AuthState.Error).message
                showError = true
            }
            else -> {
                showError = false
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Inscription", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nom d'utilisateur") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmer le mot de passe") },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        signupViewModel.signup(username, email, password)
                    } else {
                        errorMessage = "Les mots de passe ne correspondent pas"
                        showError = true
                    }
                },
                enabled = authState !is AuthState.Loading && username.isNotBlank() &&
                        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
                }
                Text("S'inscrire")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Retour à la connexion")
            }

            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))
                Snackbar {
                    Text(errorMessage)
                }
            }
        }
    }
}