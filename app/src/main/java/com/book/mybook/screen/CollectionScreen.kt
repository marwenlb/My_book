package com.book.mybook.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.book.mybook.components.AddCollectionDialog
import com.book.mybook.components.BottomNavItem
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.components.CollectionCard
import com.book.mybook.components.ErrorSnackbar
import com.book.mybook.components.LoadingOverlay
import com.book.mybook.ui.theme.BeigeColor
import com.book.mybook.viewmodel.CollectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    navController: NavController,
    userId: String,
    viewModel: CollectionViewModel = viewModel()
) {
    val bottomNavItems = listOf(
        BottomNavItem.MesLivres,
        BottomNavItem.Collection,
        BottomNavItem.Recherche
    )

    val context = LocalContext.current
    val collections by viewModel.collections.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    // Charger les collections au démarrage
    LaunchedEffect(userId) {
        viewModel.loadUserCollections(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collection") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BeigeColor,
                    titleContentColor = Color.Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = BeigeColor,
                contentColor = Color.Black,
                ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Collection")
            }
        },
        bottomBar = {
            BottomNavigationBar(navController = navController, items = bottomNavItems)
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (collections.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aucune collection trouvée. Créez-en une nouvelle!")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(collections) { collection ->
                            CollectionCard(
                                collection = collection,
                                onClick = {
                                    // Navigation vers le détail de la collection
                                    // navController.navigate("collection_detail/${collection.id}")
                                }
                            )
                        }
                    }
                }
            }

            // Afficher l'indicateur de chargement
            if (isLoading) {
                LoadingOverlay(isLoading = true)
            }
        }
    }

    // Dialogue d'ajout de collection
    AddCollectionDialog(
        showDialog = showDialog,
        collectionName = viewModel.collectionName.value,
        onNameChange = { viewModel.collectionName.value = it },
        collectionDescription = viewModel.collectionDescription.value,
        onDescriptionChange = { viewModel.collectionDescription.value = it },
        isPublic = viewModel.isPublic.value,
        onIsPublicChange = { viewModel.isPublic.value = it },
        onDismiss = { showDialog = false },
        onConfirm = {
            viewModel.createCollection(userId) {
                showDialog = false
                Toast.makeText(context, "Collection ajoutée", Toast.LENGTH_SHORT).show()
            }
        },
        isLoading = isLoading
    )
}