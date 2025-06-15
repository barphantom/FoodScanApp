package com.example.foodscanapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodscanapp.R
import com.example.foodscanapp.data.Macros
import com.example.foodscanapp.data.ProductRequest
import com.example.foodscanapp.viewmodel.SaveProductViewModel
import kotlinx.coroutines.launch


@Composable
fun SaveProductScreen(
    navController: NavController,
    viewModel: SaveProductViewModel = viewModel()
) {
    val context = LocalContext.current

    var barcode by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("") }

    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }

    var ingredients by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(64.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 128.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(stringResource(R.string.dodaj_produkt_text), style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = barcode,
                    onValueChange = { barcode = it },
                    label = { Text(stringResource(R.string.kod_kreskowy)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.nazwa_produktu)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = grams,
                    onValueChange = { grams = it },
                    label = { Text(stringResource(R.string.gramatura_g)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(stringResource(R.string.makroskladniki_na_100g), fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text(stringResource(R.string.kalorie)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text(stringResource(R.string.bialko)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text(stringResource(R.string.tluszcz)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text(stringResource(R.string.weglowodany)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text(stringResource(R.string.skladniki_oddzielone_przecinkami)) },
                    modifier = Modifier.fillMaxWidth()
                )

            }

            Button(
                onClick = {
                    if (barcode.isBlank() || name.isBlank() || grams.isBlank() ||
                        calories.isBlank() || protein.isBlank() || fat.isBlank() || carbs.isBlank()) {
                        Toast.makeText(context, "Uzupełnij wszystkie wymagane pola", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val productRequest = ProductRequest(
                        barcode = barcode,
                        name = name,
                        grams = grams.toIntOrNull() ?: 0,
                        macrosPer100g = Macros(
                            calories = calories.toDoubleOrNull() ?: 0.0,
                            protein = protein.toDoubleOrNull() ?: 0.0,
                            fat = fat.toDoubleOrNull() ?: 0.0,
                            carbs = carbs.toDoubleOrNull() ?: 0.0
                        ),
                        ingredients = ingredients.split(",").map { it.trim() }
                    )

                    coroutineScope.launch {
                        try {
                            viewModel.saveProduct(productRequest)
                            Toast.makeText(context,
                                context.getString(R.string.toast_produkt_zapisany), Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } catch (e: Exception) {
                            Toast.makeText(context,
                                context.getString(R.string.toast_blad_zapisu) + "${e.message}", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(64.dp)

            ) {
                Text(stringResource(R.string.przycisk_zapisz_produkt))
            }

        }

    }
}
