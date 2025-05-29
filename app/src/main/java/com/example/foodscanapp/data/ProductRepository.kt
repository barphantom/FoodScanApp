package com.example.foodscanapp.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.math.log

class ProductRepository {
    private val api: OpenFoodFactsApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        api = retrofit.create(OpenFoodFactsApi::class.java)
    }

    suspend fun fetchProduct(barcode: String): Product? {
        return try {
            api.getProductByBarcode(barcode)
        } catch (error: Exception) {
            null
        }
    }
}