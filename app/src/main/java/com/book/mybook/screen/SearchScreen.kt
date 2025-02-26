package com.book.mybook.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.book.mybook.ui.theme.BeigeColor

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(navController: NavController) {

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = BeigeColor,
                    contentColor = Color.Black
                ),
                onClick = { navController.navigate("barcode_scanner") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)
            ) {
                Text("Scanner le code ISBN")
            }

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = BeigeColor,
                    contentColor = Color.Black
                ),
                onClick = { navController.navigate("barcode_search") },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp)
            ) {
                Text("Entrer le code ISBN manuellement")
            }
        }
    }
}
