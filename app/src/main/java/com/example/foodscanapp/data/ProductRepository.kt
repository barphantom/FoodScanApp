package com.example.foodscanapp.data

import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class ProductRepository {
    private val api: OpenFoodFactsApi

    init {
        val client  = OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor("off", "off"))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.net/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(OpenFoodFactsApi::class.java)
    }

    suspend fun fetchProduct(barcode: String): Product? {
        return try {
            val response = api.getProductByBarcode(barcode)
            if (response.code == "product_not_found") {
                null
            } else {
                response
            }
        } catch (error: Exception) {
            Log.e("ProductRepository", "Error fetching product", error)
            null
        }
    }
}

class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private val credentials: String = Credentials.basic(username, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", credentials)
            .build()

        return chain.proceed(request)
    }
}