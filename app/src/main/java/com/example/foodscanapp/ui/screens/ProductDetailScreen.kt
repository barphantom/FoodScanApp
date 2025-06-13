package com.example.foodscanapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.utils.IngredientAnalyzer
import com.example.foodscanapp.utils.IngredientQuality
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Debugowanie w konsoli
            println("==== DEBUG PRODUKTU ====")
            println("Kod produktu: ${product.code}")
            println("Status: ${product.status}")
            println("Status verbose: ${product.statusVerbose}")

            product.product?.let { productInfo ->
                println("Nazwa produktu: ${productInfo.productName}")
                println("Nutriscore: ${productInfo.nutritionGrades}")
                println("Składniki (text): ${productInfo.ingredientsText}")

                // Nutriments
                productInfo.nutriments?.let {
                    println("Kalorie: ${it.energyKcal} kcal")
                    println("Węglowodany: ${it.carbs}g")
                    println("Tłuszcze: ${it.fats}g")
                    println("Białko: ${it.proteins}g")
                } ?: println("Brak danych o wartościach odżywczych")

                // Składniki szczegółowe
                productInfo.ingredients?.let { ingredients ->
                    println("\nSzczegółowe składniki (${ingredients.size}):")
                    ingredients.forEachIndexed { index, ingredient ->
                        println("${index + 1}. ${ingredient.text}")
                        println("   ID: ${ingredient.id}")
                        println("   Wegański: ${ingredient.vegan ?: "nieznane"}")
                        println("   Wegetariański: ${ingredient.vegetarian ?: "nieznane"}")
                    }
                } ?: println("Brak szczegółowych danych o składnikach")
            } ?: println("Brak głównych informacji o produkcie")

            // Wyświetlanie w UI
            Text("Kod produktu: ${product.code}", style = MaterialTheme.typography.headlineSmall)
            Text("Nazwa produktu: ${product.product?.productName}", style = MaterialTheme.typography.headlineSmall)
            Text("Nutriscore: ${product.product?.nutritionGrades}", style = MaterialTheme.typography.headlineSmall)
            Text("Status: ${product.status}", style = MaterialTheme.typography.headlineSmall)
            Text("Status verbose: ${product.statusVerbose}", style = MaterialTheme.typography.headlineSmall)

            // Wartości odżywcze
            product.product?.nutriments?.let {
                Text("Kalorie: ${it.energyKcal} kcal", style = MaterialTheme.typography.bodyLarge)
                Text("Węglowodany: ${it.carbs}g", style = MaterialTheme.typography.bodyLarge)
                Text("Tłuszcze: ${it.fats}g", style = MaterialTheme.typography.bodyLarge)
                Text("Białko: ${it.proteins}g", style = MaterialTheme.typography.bodyLarge)
            }

            // Składniki
            Text("Składniki: ${product.product?.ingredientsText ?: "Brak danych"}",
                style = MaterialTheme.typography.bodyLarge)

            // Szczegółowe skłądniki z oceną
            product.product?.ingredients?.forEach { ingredient ->
                val quality = IngredientAnalyzer.analyzeIngredient(ingredient.text)
                when (quality) {
                    IngredientQuality.GOOD -> Text("\uD83D\uDFE2 ${ingredient.text}")
                    IngredientQuality.BAD -> Text("\uD83D\uDD34 ${ingredient.text}")
                    IngredientQuality.UNKNOWN -> Text("⚪ ${ingredient.text}")
                }
            }

            // Szczegółowe składniki
            product.product?.ingredients?.let { ingredients ->
                Text("Szczegółowe składniki:", style = MaterialTheme.typography.bodyLarge)
                ingredients.forEachIndexed { index, ingredient ->
                    Text("${index + 1}. ${ingredient.text}", style = MaterialTheme.typography.bodyMedium)
                    Text("   Wegański: ${ingredient.vegan ?: "nieznane"}", style = MaterialTheme.typography.bodySmall)
                    Text("   Wegetariański: ${ingredient.vegetarian ?: "nieznane"}", style = MaterialTheme.typography.bodySmall)
                }
            }

//            Text("Marka: ${product.product?.brands ?: "Brak danych"}", style = MaterialTheme.typography.bodyLarge)
//            Text("Składniki: ${product.product?.ingredientsText ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
//            Text("Link do zdjęcia: ${product.product?.imageUrl ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
//            Text("Kraj pochodzenia: ${product.product?.country ?: "Brak danych"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
