package com.book.mybook.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.book.mybook.components.ErrorSnackbar
import com.book.mybook.components.LoadingOverlay
import com.book.mybook.ui.theme.BeigeColor
import com.book.mybook.viewmodel.BookDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String,
    viewModel: BookDetailViewModel = viewModel()
) {
    val book by viewModel.book.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val scrollState = rememberScrollState()

    // Charger le livre au démarrage
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book?.title ?: "Détails du livre") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BeigeColor,
                    titleContentColor = Color.Black
                )
            )
        },
        snackbarHost = {
            ErrorSnackbar(
                errorMessage = error,
                onDismiss = { viewModel.clearError() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                book?.let { bookItem ->
                    // Image de couverture
                    AsyncImage(
                        model = bookItem.coverImageUrl ?: "https://via.placeholder.com/300x450",
                        contentDescription = bookItem.title,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(300.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Titre et sous-titre
                    Text(
                        text = bookItem.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    bookItem.subtitle?.let { subtitle ->
                        if (subtitle.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = subtitle,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Auteurs
                    Text(
                        text = "Par ${bookItem.authors.joinToString(", ") { it.name }}",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Éditeurs
                    Text(
                        text = "Édité par ${bookItem.publishers.joinToString(", ") { it.name }}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Évaluation
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val filledStars = (bookItem.rate / 5 * 5).toInt()

                        repeat(filledStars) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                        }
                        repeat(5 - filledStars) {
                            Icon(
                                imageVector = Icons.Outlined.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${bookItem.rate}/5",
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Détails du livre
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF5F5F5)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "ISBN",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = bookItem.isbn)
                                }

                                Column {
                                    Text(
                                        text = "Pages",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = bookItem.numberOfPages ?: "N/A")
                                }

                                Column {
                                    Text(
                                        text = "Année",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = bookItem.publishDate ?: "N/A")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    if (!bookItem.description.isNullOrEmpty()) {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = bookItem.description,
                            fontSize = 15.sp,
                            lineHeight = 24.sp
                        )
                    } else {
                        Text(
                            text = "Aucune description disponible",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
            }

            if (isLoading) {
                LoadingOverlay(isLoading = true)
            }
        }
    }
}