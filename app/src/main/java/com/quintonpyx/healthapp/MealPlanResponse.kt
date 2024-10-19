package com.quintonpyx.healthapp

data class MealPlanResponse(
    val id: Int,
    val name: String,
    val days: List<Day>
)

data class Day(
    val day: String,
    val items: List<MealPlanItem>
)

data class MealPlanItem(
    val id: Int,
    val slot: Int,
    val type: String, // "RECIPE" or "INGREDIENTS"
    val value: MealPlanValue
)

data class MealPlanValue(
    val id: Int, // Recipe or Ingredient ID
    val title: String,
    val image: String? // Optional, for image URLs if available
)