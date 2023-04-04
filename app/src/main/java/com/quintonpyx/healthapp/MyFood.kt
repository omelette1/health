package com.quintonpyx.healthapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.quintonpyx.healthapp.helper.GeneralHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar

import java.util.*

class MyFood : AppCompatActivity() {

    private lateinit var userFoodRecyclerView: RecyclerView
    private lateinit var userFoodList: ArrayList<UserFood>
    private lateinit var adapter: UserFoodAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var database: DatabaseReference
    private lateinit var txtCalorie: TextView
    private lateinit var txtUnit: TextView
    private lateinit var user: FirebaseUser
    private lateinit var progressBar: CircularProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_food)
        user = FirebaseAuth.getInstance().currentUser!!

        txtCalorie = findViewById(R.id.tv_calories)
        database = Firebase.database.reference
        txtUnit = findViewById(R.id.txtUnit)
        progressBar = findViewById(R.id.progress_bar)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentUser = snapshot.getValue(User::class.java) as User
                    txtUnit.setText("/ " + currentUser.targetCalorie.toString() + " kcal")
                    progressBar.progressMax = currentUser.targetCalorie!!.toFloat()
                } else {


                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyFood, "Error: " + error.toString(), Toast.LENGTH_LONG).show()
            }
        }
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        var tv_calories = findViewById<TextView>(R.id.tv_calories)

        if (sharedPreferences.getString("date", "") != GeneralHelper.getTodayDate()) {
            val calorieShown = 0
            tv_calories.text = ("$calorieShown")
            progressBar.setProgressWithAnimation(calorieShown.toFloat(), 1000)

            val snapshot = database.child("user").child(user.uid)
                .addListenerForSingleValueEvent(eventListener)

            // menu code
            // Initialize and assign variable
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

            // Set Home selected

            // Set Home selected
            bottomNavigationView.selectedItemId = R.id.myFood

            // Perform item selected listener
            bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.food -> {
                        startActivity(Intent(this@MyFood, MainActivity2::class.java))
                        // override default transition from page to page
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }

                    R.id.steps -> {
                        startActivity(Intent(this@MyFood, MainActivity::class.java))
                        // override default transition from page to page
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.leaderboard -> {
                        startActivity(Intent(this@MyFood, Leaderboard::class.java))
                        // override default transition from page to page
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.myFood -> {
                        return@OnNavigationItemSelectedListener true

                    }

                    R.id.profile -> {
                        startActivity(Intent(this@MyFood, Profile::class.java))
                        // override default transition from page to page
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }

                }
                false
            })

            mAuth = FirebaseAuth.getInstance()
            mDbRef = FirebaseDatabase.getInstance().getReference()
            userFoodList = ArrayList()
            adapter = UserFoodAdapter(this, userFoodList)


            userFoodRecyclerView = findViewById(R.id.userFoodRecyclerView)
            userFoodRecyclerView.layoutManager = LinearLayoutManager(
                this@MyFood
            )
            userFoodRecyclerView.adapter = adapter


            // get data
            val userFoodListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalCalorie = 0
                    userFoodList.clear()
                    var arr = ArrayList<UserFood>()
                    for (userFoodSnapshot in snapshot.children) {
                        val userFood = userFoodSnapshot.getValue(UserFood::class.java)
                        arr.add(userFood as UserFood)
                        if (userFood.date?.equals(GeneralHelper.getTodayDate()) == true) {
                            totalCalorie += userFood.calorie!!

                        }
//                    Log.d("USERS", userSnapshot.value)
                    }

                    var sortedList = arr.sortedWith(compareBy({ it.date }))
                    for (obj in sortedList) {
                        userFoodList.add(obj)
                    }

                    userFoodList.reverse()


                    txtCalorie.setText(totalCalorie.toString())

                    adapter.notifyDataSetChanged()

                }


                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MyFood, "Error: " + error.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            database.child("userFood").orderByChild("user").equalTo(user.uid)
                .addValueEventListener(userFoodListener)


//

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


    }
}