package com.quintonpyx.healthapp.networking

import com.quintonpyx.healthapp.IngredientSearchResponse
import com.quintonpyx.healthapp.MealPlanResponse // Create this response class
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApiService {
    @GET("food/ingredients/search")
    suspend fun searchIngredients(
        @Query("query") query: String,
        @Query("number") number: Int = 10,
        @Query("sort") sort: String = "calories",
        @Query("sortDirection") sortDirection: String = "desc",
        @Query("minProteinPercent") minProteinPercent: Double? = null,
        @Query("maxFatPercent") maxFatPercent: Double? = null,
        @Query("intolerances") intolerances: String? = null,
        @Header("x-api-key") apiKey: String // Using header instead of query parameter
    ): IngredientSearchResponse

    @POST("mealplanner/{username}/mealplans?hash={hash}")
    suspend fun createMealPlan(
        @Path("username") username: String,
        @Query("hash") hash: String,
        @Body mealPlanRequest: MealPlanRequest, // Request body
        @Header("x-api-key") apiKey: String // Include your API key in the header
    ): MealPlanResponse // This should be a new data class for the response
}

// Request and response data classes
data class MealPlanRequest(
    val item: MealPlanItem
)

data class MealPlanItem(
    val id: Int,
    val type: String, // "RECIPE" or "INGREDIENTS"
    val value: MealPlanValue
)

data class MealPlanValue(
    val id: Int, // Recipe ID or Ingredient ID
    val title: String,
    // Additional fields as needed
)

// You'll need to create this response class based on the API response structure
data class MealPlanResponse(
    val id: Int,
    val name: String,
    // Add other fields based on your requirements
)
