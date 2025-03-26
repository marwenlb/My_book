package com.book.mybook

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.book.mybook.api.SessionManager
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.navigation.AuthNavGraph
import com.book.mybook.ui.theme.MyBookTheme
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

                // Vérifier si l'utilisateur est connecté et naviguez en conséquence
                LaunchedEffect(key1 = true) {
                    coroutineScope.launch {
                        if (SessionManager.isLoggedIn()) {
                            // L'utilisateur est déjà connecté, naviguez vers l'écran d'accueil
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
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