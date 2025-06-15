package com.example.foodscanapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodscanapp.R
import com.example.foodscanapp.navigation.Screen
import com.example.foodscanapp.viewmodel.ProductRequestViewModel


@Composable
fun BarcodeCheckScreen(
    viewModel: ProductRequestViewModel,
    navController: NavController
) {
    var barcode by remember { mutableStateOf("") }

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(64.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.sprawdz_produkt_po_kodzie_kreskowym), style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = barcode,
            onValueChange = { barcode = it },
            label = { Text(stringResource(R.string.label_kod_kreskowy)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (barcode.isEmpty()) {
                Toast.makeText(context, "Kod nie może być pusty", Toast.LENGTH_SHORT).show()
                return@Button
            }
            viewModel.loadProductByBarcode(barcode)
            navController.navigate(Screen.ProductRequestScreen.route)
        }) {
            Text(stringResource(R.string.przycisk_wyslij))
        }
    }
}
