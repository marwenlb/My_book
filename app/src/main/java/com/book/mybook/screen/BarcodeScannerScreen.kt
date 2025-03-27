package com.book.mybook.screen

import BookModel
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.book.mybook.api.Repository.addBookToDB
import com.book.mybook.api.SessionManager
import com.book.mybook.components.BottomNavigationBar
import com.book.mybook.components.TopBar
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@Composable
fun BarcodeScannerScreen(navController: NavController) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // State for showing loading, success, and book details dialog
    var showDialog by remember { mutableStateOf(false) }  // Indicate loading state
    var showSuccess by remember { mutableStateOf(false) } // Indicate success
    var bookDetails by remember { mutableStateOf<BookModel?>(null) } // Book details
    var isProcessing by remember { mutableStateOf(false) } // To prevent multiple API calls

    // Request camera permission
    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) -> {
                hasCameraPermission = true
            }
            else -> {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Camera executor
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // State for scanned ISBN
    var scannedIsbn by remember { mutableStateOf<String?>(null) }

    // Launch API call after scan
    LaunchedEffect(scannedIsbn) {

        scannedIsbn?.let { isbn ->
            if (isProcessing) return@LaunchedEffect

            isProcessing = true
            showDialog = true

            try {
                val token = SessionManager.getAccessToken() ?: ""
                val result = addBookToDB(token, isbn)

                if (result != null) {
                    bookDetails = result
                    showSuccess = true  // ⚠️ Activer showSuccess avant de désactiver showDialog
                }
            } catch (e: Exception) {
                Log.e("BarcodeScanner", "Erreur lors de la recherche du livre", e)
            } finally {
                showDialog = false // Désactiver après showSuccess
                isProcessing = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(title="Scanner le code ISBN", navController = navController, onLogout = {
                SessionManager.logout {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }, showBackButton = true)
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (hasCameraPermission) {
                // Camera preview
                CameraPreview(
                    context = context,
                    cameraExecutor = cameraExecutor,
                    isProcessing = isProcessing,
                    showSuccess = showSuccess,
                    scannedIsbn = scannedIsbn,
                    onBarcodeDetected = { isbn ->
                        scannedIsbn = isbn
                    }
                )


                // Rectangle zone overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }

            // Show loading dialog only while waiting for API response
            if (showDialog) {
                LoadingDialog()
            }

            // Show success dialog with book details
            if (showSuccess && bookDetails != null) {
                SuccessDialogWithDetails(
                    book = bookDetails!!,
                    onDismiss = {
                        showSuccess = false
                        bookDetails = null
                        scannedIsbn = null  // Permet de relancer un scan
                    }

                )
            }
        }
    }
}

@Composable
fun LoadingDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Ajout du livre...") },
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator()
            }
        },
        confirmButton = {}
    )
}

@Composable
fun SuccessDialogWithDetails(book: BookModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Livre ajouté") },
        text = {
            Column {
                Text("Titre : ${book.title}")
                Text("Auteur(s) : ${book.authors.joinToString(", ") { it.name }}")
                Text("ISBN : ${book.isbn}")
                Text("Description : ${book.description ?: "Pas de description disponible."}")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(
    context: Context,
    cameraExecutor: java.util.concurrent.ExecutorService,
    isProcessing: Boolean,
    showSuccess: Boolean,
    scannedIsbn: String?, // Pass scannedIsbn to track when it resets
    onBarcodeDetected: (String) -> Unit
) {
    val lifecycleOwner = rememberUpdatedState(context as androidx.lifecycle.LifecycleOwner)
    val previewView = remember { PreviewView(context) }
    val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient()

    var hasDetectedBarcode by remember { mutableStateOf(false) }

    // Reset hasDetectedBarcode when scannedIsbn is reset
    LaunchedEffect(scannedIsbn) {
        if (scannedIsbn == null) {
            hasDetectedBarcode = false // Enable scanning again
        }
    }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        if (!isProcessing && !hasDetectedBarcode && !showSuccess) {
                            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                                barcodeScanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            if (barcode.valueType == Barcode.TYPE_ISBN) {
                                                barcode.displayValue?.let { isbn ->
                                                    hasDetectedBarcode = true
                                                    onBarcodeDetected(isbn)
                                                }
                                            }
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner.value,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                Log.e("BarcodeScanner", "Error setting up the camera", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

