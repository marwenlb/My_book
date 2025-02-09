package com.book.mybook.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.book.mybook.screen.HomeScreen
import com.book.mybook.screen.LoginScreen
import com.book.mybook.screen.MesLivresScreen
import com.book.mybook.screen.SignupScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier // Ajout du padding ici
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("mes_livres") { MesLivresScreen(navController) }

    }
}



