package com.book.mybook.screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.book.mybook.api.SessionManager
import com.book.mybook.components.BottomNavItem
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.components.TopBar
import com.book.mybook.ui.theme.BeigeColor

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(navController: NavController) {
    // Bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem.MesLivres,
        BottomNavItem.Collection,
        BottomNavItem.Recherche
    )

    Scaffold(
        topBar = {
            TopBar(title="Recherche par ISBN ",navController = navController, onLogout = {
                SessionManager.logout {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            } )

        },

        bottomBar = {
            BottomNavigationBar(navController = navController )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // First Button: Scanner le code ISBN
            Button(

                onClick = {
                    navController.navigate("barcode_scanner")

                },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)            ) {
                Text("Scanner le code ISBN")

            }

            // Second Button: Entrer le code ISBN manuellement
            Button(

                onClick = {
                    navController.navigate("barcode_search")

                        // Action pour entrer le code ISBN manuellement
                },
                shape = RoundedCornerShape(24.dp),

                        modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)

            ) {
                Text("Entrer le code ISBN manuellement")
            }
        }
    }
}
