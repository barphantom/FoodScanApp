package com.example.foodscanapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodscanapp.data.ProductRequest
import com.example.foodscanapp.data.ProductResponse
import com.example.foodscanapp.data.SavedProduct
import com.example.foodscanapp.network.KtorClient
import com.example.foodscanapp.network.ProductRequestService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductRequestViewModel : ViewModel() {

    private val service = ProductRequestService(KtorClient.httpClient)

    private val _currentProduct = mutableStateOf<ProductResponse?>(null)
    val currentProduct: State<ProductResponse?> = _currentProduct

    private val _allFetchedProducts = MutableStateFlow<List<SavedProduct>>(emptyList())
    val allFetchedProducts: StateFlow<List<SavedProduct>> = _allFetchedProducts

    fun loadProductByBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                val product = service.getProduct(barcode)
                _currentProduct.value = product
            } catch (e: Exception) {
                println("Błąd ładowania produktu: ${e.message}")
                _currentProduct.value = null
            }
        }
    }

    fun getAllSavedProducts() {
        viewModelScope.launch {
            try {
                val savedProducts = service.getAllProducts()
                _allFetchedProducts.value = savedProducts
            } catch (e: Exception) {
                println("Błąd podczas ładowania produktów: ${e.message}")
            }
        }
    }

    fun deleteProduct(barcode: String) {
        viewModelScope.launch {
            try {
                service.deleteProduct(barcode)
                getAllSavedProducts()
            } catch (e: Exception) {
                println("Błąd podczas usuwania produktów: ${e.message}")
            }
        }
    }
}

