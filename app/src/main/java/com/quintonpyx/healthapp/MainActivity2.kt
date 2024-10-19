package com.quintonpyx.healthapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quintonpyx.healthapp.networking.SpoonacularApiService
import com.quintonpyx.healthapp.viewModel.MainViewModel
import com.quintonpyx.healthapp.viewModel.MainViewModelFactory
import android.widget.Button
import android.widget.EditText
import com.quintonpyx.healthapp.networking.ApiConfig
import com.quintonpyx.healthapp.service.App

class MainActivity2 : AppCompatActivity() {

    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodList: ArrayList<Ingredient>
    private lateinit var adapter: FoodAdapter
    private lateinit var edtSearch: EditText
    private lateinit var btnSearch: Button

    // Initialize the ViewModel with Factory
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).apiService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        edtSearch = findViewById(R.id.edtSearch)
        btnSearch = findViewById(R.id.btnSearch)
        foodRecyclerView = findViewById(R.id.foodRecyclerView)

        foodList = ArrayList()
        adapter = FoodAdapter(this, foodList)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)
        foodRecyclerView.adapter = adapter

        // Set up the search button functionality
        btnSearch.setOnClickListener {
            val query = edtSearch.text.toString().trim()

            if (query.isNotBlank()) {
                // Example: Pass additional parameters for API request
                mainViewModel.getFoodData(
                    query = query,
                    number = 5, // You can change this or get it from user input
                    minProteinPercent = 10.0, // Adjust as needed
                    maxFatPercent = 20.0, // Adjust as needed
                    intolerances = "gluten" // You can get this from user input if necessary
                )
            } else {
                Toast.makeText(this, "Search cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        // Subscribe to LiveData changes
        subscribe()
    }

    private fun subscribe() {
        // Observe loading state
        mainViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                btnSearch.isEnabled = false
                foodList.clear()
                foodList.add(Ingredient(0, "Loading...", "")) // Show loading message in the list
                adapter.notifyDataSetChanged()
            } else {
                btnSearch.isEnabled = true
            }
        }

        // Observe error state
        mainViewModel.isError.observe(this) { isError ->
            if (isError) {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_LONG).show()
            }
        }

        // Observe food data
        mainViewModel.foodData.observe(this) { foodData ->
            foodList.clear()
            if (foodData.results.isNotEmpty()) {
                foodList.addAll(foodData.results)
            } else {
                Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
                foodList.add(Ingredient(0, "No results found", "")) // Handle no results case
            }
            adapter.notifyDataSetChanged()
        }
    }
}
