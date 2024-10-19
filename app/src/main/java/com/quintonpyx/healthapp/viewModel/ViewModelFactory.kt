package com.quintonpyx.healthapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quintonpyx.healthapp.networking.SpoonacularApiService

class MainViewModelFactory(private val apiService: SpoonacularApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
