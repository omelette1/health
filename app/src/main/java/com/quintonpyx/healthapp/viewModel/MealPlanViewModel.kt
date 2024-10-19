package com.quintonpyx.healthapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintonpyx.healthapp.BuildConfig
import com.quintonpyx.healthapp.networking.MealPlanRequest
import com.quintonpyx.healthapp.networking.SpoonacularApiService
import kotlinx.coroutines.launch

class MealPlanViewModel(
    private val apiService: SpoonacularApiService
) : ViewModel() {

    fun createMealPlan(username: String, hash: String, mealPlanRequest: MealPlanRequest) {
        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.SPOONACULAR_API_KEY
                val response = apiService.createMealPlan(username, hash, mealPlanRequest, apiKey)
                // Handle the successful response here
            } catch (e: Exception) {
                // Handle errors here
            }
        }
    }
}
