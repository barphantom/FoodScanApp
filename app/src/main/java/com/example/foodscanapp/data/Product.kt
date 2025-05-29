package com.example.foodscanapp.data

data class Product (
    val code: String,
    val product: ProductInfo?
)

data class ProductInfo(
    val productName: String?,
    val brands: String?,
    val ingredientsText: String?,
    val country: String?,
    val imageUrl: String?
)