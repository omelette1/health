package com.quintonpyx.healthapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*
import android.graphics.drawable.Drawable
import android.util.Log
import java.io.InputStream
import java.net.URL
import android.graphics.Bitmap

import android.graphics.BitmapFactory

import android.os.AsyncTask





class OtherProfile : AppCompatActivity() {
    private lateinit var imgProfile: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtNameDetail:TextView
    private lateinit var txtSteps:TextView
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile)



//        // menu code
//        // Initialize and assign variable
//        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
//
//        // Set Home selected
//        bottomNavigationView.selectedItemId = R.id.profile
//
//        // Perform item selected listener
//        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.food ->{
//
//                    startActivity(Intent(this@OtherProfile, MainActivity2::class.java))
//                    // override default transition from page to page
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.steps ->{
//                    startActivity(Intent(this@OtherProfile, MainActivity::class.java))
//                    // override default transition from page to page
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.leaderboard -> {
//                    startActivity(Intent(this@OtherProfile, Leaderboard::class.java))
//                    // override default transition from page to page
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.myFood -> {
//                    startActivity(Intent(this@OtherProfile, MyFood::class.java))
//                    // override default transition from page to page
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true                }
//                R.id.profile -> {
//                    startActivity(Intent(this@OtherProfile, Profile::class.java))
//                    // override default transition from page to page
//                    overridePendingTransition(0, 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//
//            }
//            false
//        })

        mDbRef = FirebaseDatabase.getInstance().getReference()
        txtName = findViewById(R.id.tv_name)
        imgProfile = findViewById(R.id.imgProfile)
        txtNameDetail = findViewById(R.id.tv_name_detail)
        txtSteps = findViewById(R.id.tv_steps)

        val eventListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val currentUser = snapshot.getValue(User::class.java) as User
                    txtSteps.setText(currentUser.steps.toString())
                    txtName.setText(currentUser.name.toString())
                    txtNameDetail.setText(currentUser.name.toString())

                    DownloadImageTask(imgProfile).execute(currentUser.photoUrl)

                } else {
                    Toast.makeText(this@OtherProfile,"User does not exist",Toast.LENGTH_LONG)

                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@OtherProfile,"Error: "+error.toString(), Toast.LENGTH_LONG).show()
            }
        }

//        val getData = object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()){
//                    val currentUser = snapshot.getValue(User::)
//                }
//            }
//        }

        val snapshot = mDbRef.child("user").child(intent.getStringExtra("uid").toString())
            .addListenerForSingleValueEvent(eventListener)


    }

//    fun loadImageFromWebOperations(url: String?): Drawable? {
//        try {
//            val `is`: InputStream = URL(url).getContent() as InputStream
//            return Drawable.createFromStream(`is`, "src name")
//        } catch (e: Exception) {
//            Log.d("IMAGEERROR",e.toString())
//            return null
//        }
//    }
    private class DownloadImageTask(var bmImage: ImageView) :
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
                bmImage.setImageBitmap(result)
        }
    }
}