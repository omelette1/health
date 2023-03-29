package com.quintonpyx.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class Leaderboard : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var database:DatabaseReference
    private lateinit var edtSteps: TextView
    private lateinit var edtName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        database = Firebase.database.reference
        val edtSteps = findViewById<TextView>(R.id.edtSteps)
        val edtName = findViewById<TextView>(R.id.edtName)

        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.leaderboard

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food ->{

                    startActivity(Intent(this@Leaderboard, MainActivity2::class.java))
                // override default transition from page to page
                overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
            }
                R.id.steps ->{
                    startActivity(Intent(this@Leaderboard, MainActivity::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.leaderboard -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myFood -> {
                    startActivity(Intent(this@Leaderboard, MyFood::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true                }
                R.id.profile -> {
                    startActivity(Intent(this@Leaderboard, Profile::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })

        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this@Leaderboard
        )
        userRecyclerView.adapter = adapter

        // get data
        val userListener = object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(userSnapshot in snapshot.children){
                    val user = userSnapshot.getValue(User::class.java)
                    userList.add(user as User)
//                    Log.d("USERS", userSnapshot.value)
                }
                Collections.reverse(userList)

                // champion
                edtName.setText(userList[0].name)
                edtSteps.setText(userList[0].steps.toString()+ " steps")

                userList.removeAt(0)
                adapter.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Leaderboard,"Error: "+error.toString(),Toast.LENGTH_LONG).show()
            }
        }

        database.child("user").orderByChild("steps").addValueEventListener(userListener)

    }
}