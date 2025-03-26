package com.book.mybook

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.book.mybook.api.SessionManager
import com.book.mybook.navigation.AuthNavGraph
import com.book.mybook.ui.theme.MyBookTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyBookTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()
                var isLoggedOut by remember { mutableStateOf(false) }

                // Vérifier si l'utilisateur est connecté et naviguer en conséquence
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        if (SessionManager.isLoggedIn()) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                }

                // Observer les changements de l'état de connexion
                LaunchedEffect(Unit) {
                    SessionManager.isLoggedOut.collectLatest { loggedOut ->
                        if (loggedOut) {
                            isLoggedOut = true
                        }
                    }
                }

                // Si l'utilisateur est déconnecté, naviguer vers la page de connexion
                LaunchedEffect(isLoggedOut) {
                    if (isLoggedOut) {
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AuthNavGraph(navController = navController)
                }
            }
        }
    }
}
