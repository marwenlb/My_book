package com.book.mybook.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.book.mybook.screen.BarCodeSearchScreen
import com.book.mybook.screen.BarcodeScannerScreen
import com.book.mybook.screen.CollectionScreen
import com.book.mybook.screen.HomeScreen
import com.book.mybook.screen.JaiLivresScreen
import com.book.mybook.screen.JaiLuLivresScreen
import com.book.mybook.screen.JaimeLivresScreen
import com.book.mybook.screen.JeLisLivresScreen
import com.book.mybook.screen.LoginScreen
import com.book.mybook.screen.MesLivresScreen
import com.book.mybook.screen.SearchScreen
import com.book.mybook.screen.SignupScreen
import com.book.mybook.screen.WhishlistLivresScreen

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



