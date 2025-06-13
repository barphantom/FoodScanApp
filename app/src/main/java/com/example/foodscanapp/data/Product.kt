package com.example.foodscanapp.data
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json


data class Product (
    val code: String,
    val product: ProductInfo?,
    val status: String,
    @SerializedName("status_verbose") val statusVerbose: String
)

data class ProductInfo(
    val nutriments: Nutriments?,
    val ingredients: List<Ingredient>?,
    @SerializedName("ingredients_text") val ingredientsText: String?,
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutrition_grade_fr") val nutritionGrades: String?,
)

data class Nutriments (
    @SerializedName("energy-kcal") val energyKcal: Float?,
    @SerializedName("carbohydrates") val carbs: Float?,
    @SerializedName("fat") val fats: Float?,
    @SerializedName("proteins") val proteins: Float?,
)

data class Ingredient(
    @Json(name = "id") val id: String,
    @Json(name = "text") val text: String,
    @Json(name = "vegan") val vegan: String?,
    @Json(name = "vegetarian") val vegetarian: String?
)