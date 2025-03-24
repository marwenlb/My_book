package com.book.mybook.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.core.content.ContextCompat
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
    val sharedUrl by viewModel.sharedUrl.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showShareDialog by remember { mutableStateOf(false) }
    var currentCollectionId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadUserCollections(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collection") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
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
                contentColor = Color.Black
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter Collection")
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
                        Text("Aucune collection trouvée. Créez-en une nouvelle !")
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(collections) { collection ->
                            CollectionCard(
                                collection = collection,
                                onClick = { navController.navigate("collection_detail/${collection.id}") },
                                onShareClick = {
                                    currentCollectionId = collection.id.toLong()
                                    viewModel.clearSharedUrl() // Clear previous URL
                                    showShareDialog = true
                                }
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                LoadingOverlay(isLoading = true)
            }
        }
    }

    // Share dialog handling
    if (showShareDialog) {
        ShareCollectionDialog(
            context = context,
            currentShareUrl = sharedUrl ?: "",
            onDismiss = {
                showShareDialog = false
                viewModel.clearSharedUrl()
            },
            onShareWithPermissions = { permissions ->
                currentCollectionId?.let { id ->
                    viewModel.shareCollection(id, permissions)
                }
            }
        )
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

@Composable
fun ShareCollectionDialog(
    context: Context,
    currentShareUrl: String,
    onDismiss: () -> Unit,
    onShareWithPermissions: (List<String>) -> Unit
) {
    // Track selected permissions
    var readPermissionSelected by remember { mutableStateOf(true) }
    var writePermissionSelected by remember { mutableStateOf(false) }
    var deletePermissionSelected by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Partager la Collection") },
        text = {
            Column {
                if (currentShareUrl.isNotEmpty()) {
                    Text("Lien de partage :", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = currentShareUrl,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { copyToClipboard(context, currentShareUrl) }
                    )
                } else {
                    Text("Sélectionnez les permissions :", style = MaterialTheme.typography.bodyLarge)

                    // Permission checkboxes
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = readPermissionSelected,
                            onCheckedChange = { readPermissionSelected = it }
                        )
                        Text(
                            text = "Lecture",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = writePermissionSelected,
                            onCheckedChange = { writePermissionSelected = it }
                        )
                        Text(
                            text = "Écriture",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                    }
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = deletePermissionSelected,
                            onCheckedChange = { deletePermissionSelected = it }
                        )
                        Text(
                            text = "Suppression",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                    }

                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (currentShareUrl.isEmpty()) {
                        // Generate sharing link with selected permissions
                        val permissions = mutableListOf<String>().apply {
                            if (readPermissionSelected) add("COLLECTION_READ")
                            if (writePermissionSelected) add("COLLECTION_UPDATE")
                        }
                        onShareWithPermissions(permissions)
                    } else {
                        // Share existing link
                        shareCollection(context, currentShareUrl)
                    }
                }
            ) {
                Text(if (currentShareUrl.isEmpty()) "Générer le lien" else "Partager")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}

// Fonction pour copier le lien dans le presse-papiers
fun copyToClipboard(context: Context, text: String) {
    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("Lien de partage", text))
    Toast.makeText(context, "Lien copié", Toast.LENGTH_SHORT).show()
}

// Fonction pour partager via les apps sociales
fun shareCollection(context: Context, shareUrl: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Découvrez cette collection : $shareUrl")
        type = "text/plain"
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ajout du flag nécessaire pour certaines versions Android
    }
    context.startActivity(Intent.createChooser(shareIntent, "Partager via"))
}