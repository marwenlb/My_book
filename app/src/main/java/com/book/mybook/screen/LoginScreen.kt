package com.book.mybook.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.book.mybook.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.book.mybook.model.LoginResponse
import com.book.mybook.repository.AuthRepository
import com.book.mybook.util.SessionManager
import com.book.mybook.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController,     viewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val validationState by viewModel.validationState.collectAsState()
    val loginState by viewModel.loginState.collectAsState()

    // Check if user is already logged in
    LaunchedEffect(Unit) {
        if (sessionManager.getAccessToken() != null) {
            navController.navigate("mes_livres") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Effect to handle login result
    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthRepository.Result.Success -> {
                val result = loginState as AuthRepository.Result.Success<LoginResponse>
                sessionManager.saveTokens(result.data.accessToken, result.data.refreshToken)
                Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show()
                navController.navigate("mes_livres") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthRepository.Result.Error -> {
                val errorMsg = (loginState as AuthRepository.Result.Error).message
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "Login Image",
            modifier = Modifier
                .size(330.dp)
                .padding(bottom = 16.dp)
        )
        Text("Accédez à votre compte", fontSize = 24.sp,
            fontWeight = FontWeight.Bold )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom de l'utilisateur") },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            isError = validationState.usernameError != null,
            supportingText = { validationState.usernameError?.let { Text(it) } },


        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
             modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            isError = validationState.passwordError != null,
            supportingText = { validationState.passwordError?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),

        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(username, password)
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = loginState !is AuthRepository.Result.Loading
        ) {
            if (loginState is AuthRepository.Result.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Se connecter")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Créer un compte", color = MaterialTheme.colorScheme.primary)
        }
    }
}
