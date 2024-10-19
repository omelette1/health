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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.net.URL

class Profile : AppCompatActivity() {
    private lateinit var user: FirebaseUser
    private lateinit var imgProfile: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtUid: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtNameDetail: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtSteps: TextView
    private lateinit var edtTargetSteps: EditText
    private lateinit var edtTargetCalorie: EditText
    private lateinit var mDbRef: DatabaseReference
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("545793299744-v54dj86gie9l22rjvbbldqv9koheosh5.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.profile

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food -> {
                    startActivity(Intent(this@Profile, MainActivity2::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.steps -> {
                    startActivity(Intent(this@Profile, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.leaderboard -> {
                    startActivity(Intent(this@Profile, Leaderboard::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.myFood -> {
                    startActivity(Intent(this@Profile, MyFood::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.profile -> true
                else -> false
            }
        }

        user = FirebaseAuth.getInstance().currentUser!!
        mDbRef = FirebaseDatabase.getInstance().getReference()
        txtName = findViewById(R.id.tv_name)
        txtUid = findViewById(R.id.tv_uid)
        imgProfile = findViewById(R.id.imgProfile)
        txtPhone = findViewById(R.id.tv_phone)
        txtNameDetail = findViewById(R.id.tv_name_detail)
        txtEmail = findViewById(R.id.tv_email)
        txtSteps = findViewById(R.id.tv_steps)
        edtTargetSteps = findViewById(R.id.edtTargetSteps)
        edtTargetCalorie = findViewById(R.id.edtTargetCalorie)
        btnSave = findViewById(R.id.btn_save)
        btnLogout = findViewById(R.id.btn_logout)
        mAuth = FirebaseAuth.getInstance()

        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val currentUser = snapshot.getValue(User::class.java) as User
                    txtSteps.text = currentUser.steps.toString()
                    edtTargetSteps.setText(currentUser.targetSteps.toString())
                    edtTargetCalorie.setText(currentUser.targetCalorie.toString())
                    txtName.text = currentUser.name ?: "Unknown"
                    txtNameDetail.text = currentUser.name ?: "Unknown"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        mDbRef.child("user").child(user.uid).addListenerForSingleValueEvent(eventListener)

        user.photoUrl?.let {
            DownloadImageTask(imgProfile).execute(it.toString())
        } ?: run {
            imgProfile.setImageResource(R.drawable.profilepic) // Set a default image
        }

        txtName.text = user.displayName ?: "Unknown User"
        txtUid.text = "UID: ${user.uid}"
        txtPhone.text = user.phoneNumber ?: "No phone number"
        txtEmail.text = user.email ?: "No email"

        btnSave.setOnClickListener {
            val targetSteps = edtTargetSteps.text.toString().toIntOrNull() ?: 0
            if (targetSteps < 100) {
                Toast.makeText(this@Profile, "Target Steps must be larger than 100", Toast.LENGTH_LONG).show()
            } else {
                val childUpdates = HashMap<String, Any>().apply {
                    put("targetSteps", targetSteps)
                    put("targetCalorie", edtTargetCalorie.text.toString().toIntOrNull() ?: 0)
                }
                mDbRef.child("user").child(user.uid).updateChildren(childUpdates)
                Toast.makeText(this@Profile, "Daily Target Steps and Calorie have been saved", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogout.setOnClickListener {
            mAuth.signOut()
            googleSignInClient.signOut()
            startActivity(Intent(this@Profile, Login::class.java))
        }
    }

    private class DownloadImageTask(var bmImage: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
        override fun doInBackground(vararg p0: String?): Bitmap? {
            val urldisplay = p0[0] ?: return null // Handle null case

            return try {
                Log.d("DownloadImageTask", "URL: $urldisplay") // Log the URL
                val inputStream = URL(urldisplay).openStream() // May throw an exception
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                Log.e("DownloadImageTask", "Error loading image: ${e.message}")
                null // Return null on error
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                bmImage.setImageBitmap(result)
            } else {
                Log.e("DownloadImageTask", "Failed to load image.")
            }
        }
    }
    }
