package com.book.mybook.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.book.mybook.R
import com.book.mybook.api.SessionManager
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.components.TopBar
import com.book.mybook.ui.theme.BeigeColor
import com.book.mybook.ui.theme.Orange

@ExperimentalMaterial3Api
@Composable
fun JaimeLivresScreen(navController: NavController) {
    val books = listOf(
        BookItem("Book Title 1", "Author 1", "2022", R.drawable.ic_launcher_foreground),
        BookItem("Book Title 2", "Author 2", "2023", R.drawable.ic_launcher_foreground),
        BookItem("Book Title 3", "Author 3", "2021", R.drawable.ic_launcher_foreground)
    )

    Scaffold(
                topBar = {
            TopBar(title="J'aime",navController = navController, onLogout = {
                SessionManager.logout {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            },showBackButton = true )

        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,

                )
        },

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            books.forEach { book ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable { /* Action for book click */ },

                    colors = CardDefaults.cardColors(
                        containerColor = Orange
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Book image
                        Image(
                            painter = painterResource(id = book.imageRes),
                            contentDescription = "Book Image",
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 16.dp)
                        )

                        // Book details
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = book.author,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Text(
                                text = "Sortie: ${book.releaseDate}",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
