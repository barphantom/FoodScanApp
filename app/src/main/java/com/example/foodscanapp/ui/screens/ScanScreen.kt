package com.example.foodscanapp.ui.screens


import android.Manifest
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.foodscanapp.R
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.scanner.BarcodeAnalyzer
import com.example.foodscanapp.viewmodel.ProductRequestViewModel


@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: ProductRequestViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

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
                    it.surfaceProvider = previewView.surfaceProvider
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val cameraProvider = cameraProviderFuture.get()

                val barcodeAnalyzer = BarcodeAnalyzer { code ->
                    if (scannedCode == null) {
                        scannedCode = code
                        viewModel.loadProductByBarcode(code) // szukamy produktu
                        navController.navigate("productRequestScreen") // przechodzimy dalej
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
            Text(stringResource(R.string.wroc_przycisk))
        }
    }

}
