package com.book.mybook.screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.book.mybook.components.BottomNavItem
import com.book.mybook.components.BottomNavigationBar

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(navController: NavController) {
    val items = listOf(
        BottomNavItem.MesLivres,
        BottomNavItem.Recherche,
        BottomNavItem.Collection
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mes Livres") }) },
        bottomBar = { BottomNavigationBar(navController, items) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Bienvenue dans votre bibliothèque 📚")
        }
    }
}
