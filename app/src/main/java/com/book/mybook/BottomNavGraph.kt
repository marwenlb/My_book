package com.book.mybook
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.book.mybook.components.BottomNavItem
import com.book.mybook.screen.MesLivresScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.MesLivres.route) {
        composable(BottomNavItem.MesLivres.route) { MesLivresScreen(navController) }
        composable(BottomNavItem.Recherche.route) {  }
        composable(BottomNavItem.Collection.route) { /* CollectionScreen() */ }
    }
}
