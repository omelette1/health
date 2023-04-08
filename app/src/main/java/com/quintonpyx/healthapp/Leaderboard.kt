package com.quintonpyx.healthapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.net.URL
import java.util.*

class Leaderboard : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var database:DatabaseReference
    private lateinit var edtSteps: TextView
    private lateinit var edtName: TextView
    private lateinit var imgChamp: ImageView
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)
        database = Firebase.database.reference
        val edtSteps = findViewById<TextView>(R.id.edtSteps)
        val edtName = findViewById<TextView>(R.id.edtName)
        val imgChamp = findViewById<ImageView>(R.id.championImage)
        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        user = FirebaseAuth.getInstance().currentUser!!

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
                if(userList[0].uid==user.uid){
                    edtName.setText(userList[0].name+ " (You)")

                }else{
                    edtName.setText(userList[0].name)

                }
                edtSteps.setText(userList[0].steps.toString()+ " steps")
                val uid = userList[0].uid.toString()
                DownloadImageTask(imgChamp).execute(userList[0].photoUrl)
                edtName.setOnClickListener {
                    val intent = Intent(this@Leaderboard,OtherProfile::class.java)
                    intent.putExtra("uid",uid)
                    startActivity(intent)
                }

                userList.removeAt(0)
                adapter.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Leaderboard,"Error: "+error.toString(),Toast.LENGTH_LONG).show()
            }
        }

        database.child("user").orderByChild("steps").addValueEventListener(userListener)

    }
    private class DownloadImageTask(var imgChamp: ImageView) :
        AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            val urldisplay = p0[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
                e.printStackTrace()
            }
            return mIcon11
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null)
                imgChamp.setImageBitmap(result)
        }
    }
}