package com.example.foodscanapp.network


import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json


object KtorClient {
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }

        install(DefaultRequest) {
            runBlocking {
                val user = FirebaseAuth.getInstance().currentUser
                val token = try {
                    user?.getIdToken(false)?.await()?.token
                } catch (e: Exception) {
                    println("Błąd pobierania tokenu: ${e.message}")
                    null
                }

                if (!token.isNullOrEmpty()) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }
    }
}




