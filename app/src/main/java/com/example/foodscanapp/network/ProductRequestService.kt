package com.example.foodscanapp.network

import com.example.foodscanapp.data.SavedProduct
import com.example.foodscanapp.data.ProductRequest
import com.example.foodscanapp.data.ProductResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ProductRequestService(private val client: HttpClient) {
//    Dla telefonu
//    private val baseUrl = "http://192.168.23.97:8080/"
//    Dla wifi
    private val baseUrl = "http://192.168.0.150:8080/"

    suspend fun getProduct(barcode: String): ProductResponse? {
        return try {
            client.get("${baseUrl}product/$barcode").body()
        } catch (e: Exception) {
            if (e.message?.contains("404") == true) {
                null
            } else {
                throw e
            }
        }
    }

    suspend fun getAllProducts(): List<SavedProduct> {
        return client.get("${baseUrl}product/all").body()
    }

    suspend fun saveProduct(product: ProductRequest): String {
        val response: HttpResponse = client.post("${baseUrl}product/save") {
            contentType(ContentType.Application.Json)
            setBody(product)
        }
        return response.bodyAsText()
    }

    suspend fun deleteProduct(barcode: String): String {
        val response: HttpResponse = client.delete("${baseUrl}product/$barcode")
        return response.bodyAsText()
    }
}

