package com.quintonpyx.healthapp

data class IngredientSearchResponse(
    val results: List<Ingredient>, // This property holds the list of ingredients
    val offset: Int,
    val number: Int,
    val totalResults: Int
)

data class Ingredient(
    val id: Int,
    val name: String,
    val image: String
)

