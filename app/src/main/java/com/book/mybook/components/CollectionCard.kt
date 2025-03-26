package com.book.mybook.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.book.mybook.api.Model.CollectionItem
import com.book.mybook.ui.theme.BeigeColor
import com.book.mybook.ui.theme.Mandarine
import com.book.mybook.ui.theme.Orange

@Composable
fun CollectionCard(collection: CollectionItem, onClick: () -> Unit = {}, onShareClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Mandarine),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = collection.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = collection.description, fontSize = 14.sp, color = Color.White)
                }

                IconButton(onClick = onShareClick) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Partager", tint =  Color.White)
                }
            }
        }
    }
}

@Composable
fun AddCollectionDialog(
    showDialog: Boolean,
    collectionName: String,
    onNameChange: (String) -> Unit,
    collectionDescription: String,
    onDescriptionChange: (String) -> Unit,
    isPublic: Boolean,
    onIsPublicChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isLoading: Boolean = false
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Créer une Collection") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        value = collectionName,
                        onValueChange = onNameChange,
                        label = { Text("Nom",fontWeight = FontWeight.Bold) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading,

                    )
                    TextField(
                        value = collectionDescription,
                        onValueChange = onDescriptionChange,
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = isPublic,
                            onCheckedChange = onIsPublicChange,
                            enabled = !isLoading
                        )
                        Text("Public")
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirm,
                    enabled = !isLoading && collectionName.isNotBlank()
                ) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,
                    enabled = !isLoading
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}

@Composable
fun ErrorSnackbar(
    errorMessage: String?,
    onDismiss: () -> Unit
) {
    errorMessage?.let {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = onDismiss) {
                    Text("OK")
                }
            }
        ) {
            Text(text = it)
        }
    }
}

@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}