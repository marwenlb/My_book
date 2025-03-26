package com.book.mybook.components
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    data object MesLivres : BottomNavItem(
        route = "mes_livres",
        icon = Icons.Default.AccountBox,
        label = "Mes Livres"
    )

    data object Collection : BottomNavItem(
        route = "collection",
        icon = Icons.Default.Menu,
        label = "Collection"
    )
    data object Recherche : BottomNavItem(
        route = "recherche",
        icon = Icons.Default.Search,
        label = "Recherche"
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth().height(120.dp)
        .windowInsetsPadding(WindowInsets(0)), // Remove system bar padding// Remove unnecessary padding
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val selectedColor = MaterialTheme.colorScheme.onPrimary
        val unselectedColor = MaterialTheme.colorScheme.onPrimary
        val selectedBackgroundColor = Color(0xFFeca753) // Light Orange

        val bottomNavItems = listOf(
            BottomNavItem.MesLivres,
            BottomNavItem.Collection,
            BottomNavItem.Recherche,
        )

        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) selectedColor else unselectedColor
                    )
                },
                label = {
                    Text(
                        item.label,
                        color = if (currentRoute == item.route) selectedColor else unselectedColor
                    )
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    unselectedIconColor = unselectedColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = unselectedColor,
                    indicatorColor = selectedBackgroundColor // Background when selected
                )
            )
        }
    }
}
