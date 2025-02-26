package com.book.mybook.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.book.mybook.screen.*
import com.book.mybook.util.SessionManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(navController: NavHostController, context: Context, modifier: Modifier = Modifier) {
    val sessionManager = SessionManager(context)

    // Get the current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "My Book"

    Scaffold(
        topBar = {
            if (currentRoute !in listOf("login", "signup")) {
                TopBar(
                    title = getScreenTitle(currentRoute),
                    navController = navController,
                    onLogout = {
                        sessionManager.clearTokens()
                        navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    },
                    showBackButton = currentRoute in listOf(
                        "barcode_scanner",
                        "barcode_search",
                        "jai_livres",
                        "wishlist_livres",
                        "jai_lu_livres",
                        "je_lis_livres",
                        "jaime_livres"
                    )
                )
            }
        },
        bottomBar = {
            if (currentRoute !in listOf("login", "signup")) {
                BottomNavigationBar(navController)
            }
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = modifier.padding(top = 48.dp)

        ) {
            composable("login") { LoginScreen(navController) }
            composable("signup") { SignupScreen(navController) }
            composable("mes_livres") { MesLivresScreen(navController) }
             composable("collection") { CollectionScreen(navController) }
            composable("recherche") { SearchScreen(navController) }
            composable("barcode_scanner") { BarcodeScannerScreen(navController) }
            composable("barcode_search") { BarCodeSearchScreen(navController) }
            composable("jai_livres") { JaiLivresScreen(navController) }
            composable("wishlist_livres") { WhishlistLivresScreen(navController) }
            composable("jai_lu_livres") { JaiLuLivresScreen(navController) }
            composable("je_lis_livres") { JeLisLivresScreen(navController) }
            composable("jaime_livres") { JaimeLivresScreen(navController) }
        }
    }
}

/**
 * Returns screen title based on the route name
 */
fun getScreenTitle(route: String?): String {
    return when (route) {
        "mes_livres" -> "Mes Livres"
        "settings" -> "Paramètres"
        "collection" -> "Ma Collection"
        "recherche" -> "Recherche"
        "barcode_scanner" -> "Scanner"
        "barcode_search" -> "Rechercher par Code"
        "jai_livres" -> "J’ai ces livres"
        "wishlist_livres" -> "Wishlist"
        "jai_lu_livres" -> "Livres lus"
        "je_lis_livres" -> "Je lis"
        "jaime_livres" -> "J’aime ces livres"
        else -> "My Book"
    }
}
