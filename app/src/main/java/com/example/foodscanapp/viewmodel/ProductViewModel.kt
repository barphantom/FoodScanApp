package com.example.foodscanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodscanapp.data.Product
import com.example.foodscanapp.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    fun searchProduct(barcode: String) {
        viewModelScope.launch {
            val result = repository.fetchProduct(barcode)
            _product.value = result
        }
    }
}