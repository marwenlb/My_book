package com.book.mybook.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.book.mybook.navigation.BottomNavItem
import com.book.mybook.navigation.BottomNavigationBar
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text


data class CategoryItem(
    val name: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MesLivresScreen(navController: NavController) {
    val categories = listOf(
        CategoryItem("J'ai", Icons.Default.Menu),
        CategoryItem("Wishlist", Icons.Default.Favorite),
        CategoryItem("J'ai lu", Icons.Default.CheckCircle),
        CategoryItem("Je lis", Icons.Default.AccountBox),
        CategoryItem("J'aime", Icons.Default.Star)
    )

    val bottomNavItems = listOf(
        BottomNavItem.MesLivres,
        BottomNavItem.Collection,
        BottomNavItem.Recherche
    )

    Scaffold { innerPadding ->
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
                            when (category.name) {
                                "J'ai" -> navController.navigate("jai_livres")
                                "Wishlist" -> navController.navigate("wishlist_livres")
                                "J'ai lu" -> navController.navigate("jai_lu_livres")
                                "Je lis" -> navController.navigate("je_lis_livres")
                                "J'aime" -> navController.navigate("jaime_livres")
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary
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
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}
