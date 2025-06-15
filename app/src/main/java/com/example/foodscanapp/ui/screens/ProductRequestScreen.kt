package com.example.foodscanapp.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodscanapp.R
import com.example.foodscanapp.data.Ingredient
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.viewmodel.ProductRequestViewModel


@Composable
fun ProductRequestScreen(
    viewModel: ProductRequestViewModel,
    navController: NavController
) {
    val product = viewModel.currentProduct.value

    if (product == null) {
        Text(stringResource(R.string.brak_danych_produktu), modifier = Modifier.padding(16.dp))
    } else {
        Box(modifier = Modifier.fillMaxSize()) {

            // Przewijana zawartość
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 72.dp) // miejsce na przycisk
            ) {
                Text(product.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(R.string.nazwa_produktu_dwukropek) + product.name)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Makroskładniki w całości produktu:", fontWeight = FontWeight.Bold)
                Text("Kalorie: ${product.macros.calories} kcal")
                Text("Białko: ${product.macros.protein} g")
                Text("Tłuszcz: ${product.macros.fat} g")
                Text("Węglowodany: ${product.macros.carbs} g")

                Spacer(modifier = Modifier.height(24.dp))

                Text("Składniki:", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    product.ingredients.forEach { ingredient ->
                        IngredientChip(ingredient)
                    }
                }
            }

            // Przyklejony przycisk
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
}


@Composable
fun IngredientChip(ingredient: Ingredient) {
    val backgroundColor = when (ingredient.tag.lowercase()) {
        "green" -> Color(0xFFD0F0C0) // Jasnozielony
        "red" -> Color(0xFFFFC0C0)   // Jasnoczerwony
        "neutral" -> Color(0xFFE0E0E0) // Szary
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = MaterialTheme.shapes.small)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = ingredient.name, style = MaterialTheme.typography.bodySmall)
    }
}
