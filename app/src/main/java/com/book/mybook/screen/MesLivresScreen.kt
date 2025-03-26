package com.book.mybook.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.book.mybook.api.SessionManager
import com.book.mybook.components.BottomNavItem
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.components.TopBar
import com.book.mybook.ui.theme.BeigeColor
import com.book.mybook.ui.theme.Orange

data class CategoryItem(
    val name: String,
    val icon: ImageVector
)

@Composable
fun MesLivresScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()

    // Liste des catégories avec leurs icônes associées
    val categories = listOf(
        CategoryItem("J'ai", Icons.Default.Menu),
        CategoryItem("Wishlist", Icons.Default.Favorite),
        CategoryItem("J'ai lu", Icons.Default.CheckCircle),
        CategoryItem("Je lis", Icons.Default.AccountBox),
        CategoryItem("J'aime", Icons.Default.Star)
    )

    // Bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem.MesLivres,
        BottomNavItem.Collection,
        BottomNavItem.Recherche
    )

    Scaffold(
        topBar = {
            TopBar(title="Mes livres",navController = navController, onLogout = {
                SessionManager.logout {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            } )

        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Button(onClick = {
            SessionManager.logout {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }) {
            Text("Se déconnecter")
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            categories.forEach { category ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {

                            if (category.name == "J'ai") {

                                navController.navigate("jai_livres")

                            } else if (category.name == "Wishlist") {
                                navController.navigate("wishlist_livres")
                            }
                            else if (category.name == "J'ai lu") {

                                navController.navigate("jai_lu_livres")

                            }
                            else if (category.name == "Je lis") {
                                navController.navigate("je_lis_livres")

                            }

                            else if (category.name == "J'aime") {
                            navController.navigate("jaime_livres")
                            }


                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Orange
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start

                    ) {
                        Icon(
                            imageVector = category.icon,
                            contentDescription = category.name,
                            modifier = Modifier.size(32.dp),
                            tint = Color.White,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}
