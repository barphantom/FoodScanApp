package com.example.foodscanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodscanapp.navigation.AppNavigation
import com.example.foodscanapp.ui.theme.FoodScanAppTheme
import com.example.foodscanapp.viewmodel.ProductViewModel

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodscanapp.data.SettingsDataStore
import com.example.foodscanapp.utils.LocaleManager
import com.example.foodscanapp.viewmodel.SettingsViewModel
import android.Manifest

import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val language = prefs.getString("language", "pl") ?: "pl"
        val updatedContext = LocaleManager.setLocale(newBase, language)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        val settingsDataStore = SettingsDataStore(applicationContext)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return SettingsViewModel(settingsDataStore) as T
                    }
                }
            )

            val productViewModel: ProductViewModel = viewModel()

            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            FoodScanAppTheme(darkTheme = isDarkTheme) {
                AppNavigation(
                    productViewModel = productViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    FoodScanAppTheme {
//        Greeting("Android")
//        AppNavigation()
//    }
}
