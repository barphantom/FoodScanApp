package com.example.foodscanapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.viewmodel.ProductViewModel

@Composable
fun ProductDetailScreen(
    viewModel: ProductViewModel,
    navController: NavController
) {
    val product = viewModel.product.collectAsState().value


    if (product == null) {
        // Jeśli nie ma produktu (np. bezpośrednie wejście na ekran)
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Brak danych produktu")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.Home.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Wróć")
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Nazwa produktu: ${product.product?.productName}", style = MaterialTheme.typography.headlineSmall)
            Text("Marka: ${product.product?.brands ?: "Brak danych"}", style = MaterialTheme.typography.bodyLarge)
            Text("Składniki: ${product.product?.ingredientsText ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
            Text("Link do zdjęcia: ${product.product?.imageUrl ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
            Text("Kraj pochodzenia: ${product.product?.country ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
