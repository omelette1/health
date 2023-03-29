package com.quintonpyx.healthapp

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.quintonpyx.healthapp.FoodAdapter
import com.quintonpyx.healthapp.viewModel.MainViewModel

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var foodRecyclerView: RecyclerView
    private lateinit var foodList: ArrayList<Food>
    private lateinit var adapter: FoodAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mainViewModel: MainViewModel

    private lateinit var edtSearch:EditText
    private lateinit var btnSearch:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.food

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food ->
                    return@OnNavigationItemSelectedListener true

                R.id.steps ->{
                    startActivity(Intent(this@MainActivity2, MainActivity::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.leaderboard -> {
                    startActivity(Intent(this@MainActivity2, Leaderboard::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myFood -> {
                    startActivity(Intent(this@MainActivity2, MyFood::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    startActivity(Intent(this@MainActivity2, Profile::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        foodList = ArrayList()
        adapter = FoodAdapter(this, foodList)



        val edtSearch = findViewById<EditText>(R.id.edtSearch)
        val btnSearch = findViewById<Button>(R.id.btnSearch)

        foodRecyclerView = findViewById(R.id.foodRecyclerView)
        foodRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity2
        )
        foodRecyclerView.adapter = adapter

        var searchKey = edtSearch.text
        mainViewModel = MainViewModel()

        // fetch data from api
        btnSearch.setOnClickListener {


                if (searchKey.isNullOrEmpty() or searchKey.isNullOrBlank()) {
//                    searchKey.error = "Field can't be null"
                Toast.makeText(this@MainActivity2,"Search cannot be empty",Toast.LENGTH_LONG).show()
            }else{
                    mainViewModel.getFoodData(searchKey.toString())

                }
            }
        subscribe(btnSearch)

        }

// TODO: menu
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.logout) {
//            mAuth.signOut()
//            val intent = Intent(this@MainActivity, Login::class.java)
//            // finish() calls onDestroy(), kill activities
//            finish()
//            startActivity(intent)
//            return true
//        }
//        return true
////        return super.onOptionsItemSelected(item)
//    }


    private fun subscribe(btnSearch:Button) {
        mainViewModel.isLoading.observe(this) { isLoading ->
            // Is sending the API request
            foodList.add(Food("Searching...",0))
            // disable search button when searching
            btnSearch?.isEnabled = false
            btnSearch?.isClickable = false
            adapter.notifyDataSetChanged()
        }

        mainViewModel.isError.observe(this) { isError ->
            // Encountered an error in the process
            if(isError){
                Toast.makeText(this,"Unexpected error occured",Toast.LENGTH_LONG).show()

            }
        }

        mainViewModel.foodData.observe(this) { foodData ->
            // reset search button
            btnSearch?.isEnabled = true
            btnSearch?.isClickable = true
            // Display food data to the UI
            foodList.clear()
            for(item in foodData.hints){
                foodList.add(Food(item.food.label,item.food.nutrients.enerckcal.toInt()))
//                Log.d("HAHA", item.food.label)

            }
            // this is needed to notify adapter that food array has been changed
            adapter.notifyDataSetChanged()
        }
    }
}