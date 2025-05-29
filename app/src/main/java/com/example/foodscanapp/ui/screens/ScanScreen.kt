package com.example.foodscanapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodscanapp.scanner.CameraPreview


import android.Manifest
import android.content.pm.PackageManager
import android.widget.Button
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.scanner.BarcodeAnalyzer
import com.example.foodscanapp.viewmodel.ProductViewModel


//@Composable
//fun ScanScreen(
//    onScanned: (String) -> Unit,
//) {
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    var scannedValue by remember { mutableStateOf<String?>(null) }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        CameraPreview(
//            modifier = Modifier.fillMaxSize(),
//            context = context,
//            lifecycleOwner = lifecycleOwner,
//            onBarcodeScanned = { value ->
//                if (scannedValue == null) {
//                    scannedValue = value
//                    Toast.makeText(context, "Zeskanowano: $value", Toast.LENGTH_SHORT).show()
//                    onScanned(value)
//                }
//            }
//        )
//
//        scannedValue?.let {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Text(text = "Kod: $it", style = MaterialTheme.typography.bodyLarge)
//                Spacer(modifier = Modifier.height(8.dp))
//                Button(onClick = { scannedValue = null }) {
//                    Text("Skanuj ponownie")
//                }
//            }
//        }
//    }
//}
@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var scannedCode by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Sprawdź uprawnienia do kamery
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

            AndroidView(factory = { ctx ->
                val previewView = PreviewView(ctx)
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val cameraProvider = cameraProviderFuture.get()

                val barcodeAnalyzer = BarcodeAnalyzer { code ->
                    if (scannedCode == null) {
                        scannedCode = code
                        viewModel.searchProduct(code) // szukamy produktu
                        navController.navigate("productDetail") // przechodzimy dalej
                    }
                }

                val imageAnalysis = androidx.camera.core.ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx), barcodeAnalyzer)
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (exc: Exception) {
                    exc.printStackTrace()
                }

                previewView
            })
        } else {
            Text(text = "Brak dostępu do kamery")
        }

        Button(
            onClick = { navController.navigate(Screen.Home.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(128.dp)
        ) {
            Text("Wróć")
        }
    }

}
