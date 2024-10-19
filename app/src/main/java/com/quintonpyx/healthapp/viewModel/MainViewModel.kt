package com.quintonpyx.healthapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quintonpyx.healthapp.BuildConfig
import com.quintonpyx.healthapp.IngredientSearchResponse
import com.quintonpyx.healthapp.networking.SpoonacularApiService
import kotlinx.coroutines.launch

class MainViewModel(private val apiService: SpoonacularApiService) : ViewModel() {

    private val _foodData = MutableLiveData<IngredientSearchResponse>()
    val foodData: LiveData<IngredientSearchResponse> get() = _foodData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun getFoodData(
        query: String,
        number: Int,
        minProteinPercent: Double? = null,
        maxFatPercent: Double? = null,
        intolerances: String? = null
    ) {
        _isLoading.value = true
        _isError.value = false

        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.SPOONACULAR_API_KEY // Ensure your API key is set in BuildConfig
                val response = apiService.searchIngredients(
                    query = query,
                    number = number,
                    minProteinPercent = minProteinPercent,
                    maxFatPercent = maxFatPercent,
                    intolerances = intolerances,
                    apiKey = apiKey
                )
                _foodData.value = response // Set the food data
            } catch (e: Exception) {
                _isError.value = true // Indicate an error occurred
            } finally {
                _isLoading.value = false // Reset loading state
            }
        }
    }
}
