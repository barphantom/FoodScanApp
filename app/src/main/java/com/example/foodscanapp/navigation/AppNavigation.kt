package com.example.foodscanapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.foodscanapp.ui.screens.AllProductsScreen
import com.example.foodscanapp.ui.screens.BarcodeCheckScreen

import com.example.foodscanapp.ui.screens.LoginScreen
import com.example.foodscanapp.ui.screens.HomeScreen
import com.example.foodscanapp.ui.screens.ProductDetailScreen
import com.example.foodscanapp.ui.screens.ProductRequestScreen
import com.example.foodscanapp.ui.screens.RegistrationScreen
import com.example.foodscanapp.ui.screens.SaveProductScreen
import com.example.foodscanapp.ui.screens.ScanScreen
import com.example.foodscanapp.viewmodel.ProductRequestViewModel
import com.example.foodscanapp.viewmodel.ProductViewModel
import com.example.foodscanapp.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Scan: Screen("scan")
    object ProductDetail: Screen("productDetail")
    object ProductRequestScreen: Screen("productRequestScreen")
    object SaveProductScreen: Screen("saveProductScreen")
    object BarcodeCheckScreen: Screen("barcodeCheckScreen")
    object AllProductsScreen: Screen("allProductsScreen")
}


@Composable
fun AppNavigation(
    productViewModel: ProductViewModel,
    settingsViewModel: SettingsViewModel,
    productRequestViewModel: ProductRequestViewModel
) {
    val navController = rememberNavController()
    val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

    NavHost(
        navController = navController,
        startDestination = if (isUserLoggedIn) Screen.Home.route else Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegistrationScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                },
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToScan = {
                    navController.navigate(Screen.Scan.route)
                },
                navController = navController,
                viewModel = settingsViewModel
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(
                navController = navController,
                viewModel = productRequestViewModel
            )
        }

        composable(Screen.ProductDetail.route) {
            ProductDetailScreen(
                productViewModel,
                navController = navController
            )
        }

        composable(Screen.ProductRequestScreen.route) {
            ProductRequestScreen(
                viewModel = productRequestViewModel,
                navController = navController
            )
        }

        composable(Screen.SaveProductScreen.route) {
            SaveProductScreen(
                navController = navController
            )
        }

        composable(Screen.BarcodeCheckScreen.route) {
            BarcodeCheckScreen(
                viewModel = productRequestViewModel,
                navController = navController
            )
        }

        composable(Screen.AllProductsScreen.route) {
            AllProductsScreen(
                viewModel = productRequestViewModel,
                navController = navController
            )
        }

    }
}
