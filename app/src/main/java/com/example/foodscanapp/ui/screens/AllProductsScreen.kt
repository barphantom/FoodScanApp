package com.example.foodscanapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodscanapp.data.SavedProduct
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.viewmodel.ProductRequestViewModel


@Composable
fun AllProductsScreen(
    navController: NavController,
    viewModel: ProductRequestViewModel
) {
    val allProducts by viewModel.allFetchedProducts.collectAsState()
    var productToDelete by remember { mutableStateOf<SavedProduct?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getAllSavedProducts()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (allProducts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Brak zapisanych produktów.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allProducts) { product ->
                    ProductCard(product = product, onDelete = { productToDelete = it })
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(64.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Powrót do ekranu głównego")
        }
    }

    if (productToDelete != null) {
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Potwierdzenie usunięcia") },
            text = { Text("Czy na pewno chcesz usunąć produkt '${productToDelete!!.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(productToDelete!!.barcode)
                        productToDelete = null
                    }
                ) {
                    Text("Tak", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Anuluj")
                }
            }
        )
    }
}

@Composable
fun ProductCard(product: SavedProduct, onDelete: (SavedProduct) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(product.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Kod kreskowy: ${product.barcode}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Waga: ${product.weight} g")
            Text("Kalorie: ${product.calories} kcal")
            Text("Białko: ${product.protein} g")
            Text("Tłuszcz: ${product.fat} g")
            Text("Węglowodany: ${product.carbs} g")

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onDelete(product) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Usuń")
            }
        }
    }
}
