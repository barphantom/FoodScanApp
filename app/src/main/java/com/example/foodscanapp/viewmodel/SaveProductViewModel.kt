package com.example.foodscanapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodscanapp.data.ProductRequest
import com.example.foodscanapp.network.KtorClient
import com.example.foodscanapp.network.ProductRequestService


class SaveProductViewModel : ViewModel() {
    private val service = ProductRequestService(KtorClient.httpClient)

    suspend fun saveProduct(productRequest: ProductRequest) {
        try {
            service.saveProduct(productRequest)
        } catch (e: Exception) {
            println("Błąd zapisywania: ${e.message}")
        }
    }
}