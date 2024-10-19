package com.quintonpyx.healthapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.net.URL
import java.util.*

class Leaderboard : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var database: DatabaseReference
    private lateinit var edtSteps: TextView
    private lateinit var edtName: TextView
    private lateinit var imgChamp: ImageView
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Initialize Firebase database reference
        database = Firebase.database.reference

        // Find views by ID
        edtSteps = findViewById(R.id.edtSteps)
        edtName = findViewById(R.id.edtName)
        imgChamp = findViewById(R.id.championImage)

        // Initialize bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        user = FirebaseAuth.getInstance().currentUser!!

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.leaderboard

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food -> {
                    startActivity(Intent(this, MainActivity2::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.steps -> {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.leaderboard -> true
                R.id.myFood -> {
                    startActivity(Intent(this, MyFood::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                R.id.profile -> {
                    startActivity(Intent(this, Profile::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    })
                    true
                }
                else -> false
            }
        }

        // Initialize user list and adapter
        userList = ArrayList()
        adapter = UserAdapter(this, userList)
        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        // Get data from Firebase
        val userListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                Collections.reverse(userList)

                // Champion display logic
                if (userList.isNotEmpty()) {
                    if (userList[0].uid == user.uid) {
                        edtName.text = "${userList[0].name} (You)"
                    } else {
                        edtName.text = userList[0].name
                    }
                    edtSteps.text = "${userList[0].steps} steps"
                    val uid = userList[0].uid
                    DownloadImageTask(imgChamp).execute(userList[0].photoUrl)

                    // On champion name click, go to other profile
                    edtName.setOnClickListener {
                        val intent = Intent(this@Leaderboard, OtherProfile::class.java)
                        intent.putExtra("uid", uid)
                        startActivity(intent)
                    }

                    userList.removeAt(0) // Remove champion from the list
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Leaderboard, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        database.child("user").orderByChild("steps").addValueEventListener(userListener)
    }

    private class DownloadImageTask(private val imgChamp: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            val urldisplay = p0[0]
            return try {
                val input = URL(urldisplay).openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                Log.e("Error", e.message ?: "Error loading image")
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                imgChamp.setImageBitmap(result)
            }
        }
    }
}
