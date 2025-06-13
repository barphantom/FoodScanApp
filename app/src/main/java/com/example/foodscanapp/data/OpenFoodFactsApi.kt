package com.example.foodscanapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}?fields=product_name,nutriscore_data,nutriments,ingredients_text,ingredients")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("lc") languageCode: String = "pl",
    ): Product
}
