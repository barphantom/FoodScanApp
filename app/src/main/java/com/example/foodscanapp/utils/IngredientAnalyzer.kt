package com.example.foodscanapp.utils

import androidx.compose.runtime.key


object IngredientAnalyzer {
    private val badKeywords = listOf("sucre", "sirop", "E621", "aspartame", "huile de palme")
    private val goodKeywords = listOf("tomates", "fruits", "l'eau", "amidon", "cacao")

    fun isBad(ingredient: String): Boolean {
        return badKeywords.any { keyword -> ingredient.contains(keyword, ignoreCase = true) }
    }

    fun isGood(ingredient: String): Boolean {
        return goodKeywords.any { keyword -> ingredient.contains(keyword, ignoreCase = true) }
    }

    fun analyzeIngredient(ingredient: String): IngredientQuality {
        return if (isGood(ingredient)) {
            IngredientQuality.GOOD
        } else if (isBad(ingredient)) {
            IngredientQuality.BAD
        } else {
            IngredientQuality.UNKNOWN
        }
    }
}

enum class IngredientQuality {
    GOOD,
    BAD,
    UNKNOWN
}