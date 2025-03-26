package com.book.mybook.navigation

import LoginScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.book.mybook.api.SessionManager
import com.book.mybook.screen.BarCodeSearchScreen
import com.book.mybook.screen.BarcodeScannerScreen
import com.book.mybook.screen.BookDetailScreen
import com.book.mybook.screen.CollectionDetailScreen
import com.book.mybook.screen.CollectionScreen
import com.book.mybook.screen.HomeScreen
import com.book.mybook.screen.JaiLivresScreen
import com.book.mybook.screen.JaiLuLivresScreen
import com.book.mybook.screen.JaimeLivresScreen
import com.book.mybook.screen.JeLisLivresScreen
import com.book.mybook.screen.SearchScreen
import com.book.mybook.screen.SignupScreen
import com.book.mybook.screen.WhishlistLivresScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {

    val context = navController.context
    var userId by remember { mutableStateOf<String?>(null) }

    // Récupérer l'userId au démarrage du NavGraph
    LaunchedEffect(Unit) {
        SessionManager.init(context)
        userId = SessionManager.getUserId()
    }
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier // Ajout du padding ici
    ) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("mes_livres") { HomeScreen(navController) }
        composable("collection") {
            CollectionScreen(navController, userId ?: "")
        }

        composable("collection_detail/{collectionId}") { backStackEntry ->
            val collectionId = backStackEntry.arguments?.getString("collectionId") ?: ""
            CollectionDetailScreen(navController = navController, collectionId = collectionId)
        }
        composable("book_detail/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailScreen(navController = navController, bookId = bookId)
        }
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



