package com.quintonpyx.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.quintonpyx.chatapplication.FoodAdapter
import java.util.*

class MainActivity2 : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var foodList: ArrayList<Food>
    private lateinit var adapter: FoodAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        foodList = ArrayList()
        adapter = FoodAdapter(this, foodList)

        // TODO
//        foodRecyclerView = findViewById(R.id.foodRecyclerView)
//        foodRecyclerView.layoutManager = LinearLayoutManager(this)
//        foodRecyclerView.adapter = adapter

       // TODO
        // fetch data from api

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