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





class Profile : AppCompatActivity() {
    private lateinit var user: FirebaseUser
    private lateinit var imgProfile: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtUid: TextView
    private lateinit var txtPhone:TextView
    private lateinit var txtNameDetail:TextView
    private lateinit var txtEmail:TextView
    private lateinit var txtSteps:TextView
    private lateinit var edtTargetSteps:EditText
    private lateinit var mDbRef: DatabaseReference
    private lateinit var btnSave: Button
    private lateinit var btnLogout: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("545793299744-v54dj86gie9l22rjvbbldqv9koheosh5.apps.googleusercontent.com").requestEmail().build()
//          R.string not updated
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(R.string.default_web_client_id).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.profile

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food ->{

                    startActivity(Intent(this@Profile, MainActivity2::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.steps ->{
                    startActivity(Intent(this@Profile, MainActivity::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.leaderboard -> {
                    startActivity(Intent(this@Profile, Leaderboard::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myFood -> {
                    startActivity(Intent(this@Profile, MyFood::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true                }
                R.id.profile -> {

                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })

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
        btnSave = findViewById(R.id.btn_save)
        btnLogout = findViewById(R.id.btn_logout)
        mAuth = FirebaseAuth.getInstance()

        val eventListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val currentUser = snapshot.getValue(User::class.java) as User
                    txtSteps.setText(currentUser.steps.toString())
                    edtTargetSteps.setText(currentUser.targetSteps.toString())
                } else {


                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Profile,"Error: "+error.toString(), Toast.LENGTH_LONG).show()
            }
        }

        val snapshot = mDbRef.child("user").child(user.uid)
            .addListenerForSingleValueEvent(eventListener)

        DownloadImageTask(imgProfile).execute(user.photoUrl.toString())
        txtName.setText(user?.displayName)
        txtUid.setText("UID: "+user?.uid)
        txtNameDetail.setText(user?.displayName)
        txtPhone.setText(user?.phoneNumber)
        txtEmail.setText(user?.email)
        btnSave.setOnClickListener {
            val childUpdates= HashMap<String,Any>()
            childUpdates.put("targetSteps",edtTargetSteps.text.toString().toInt())
            mDbRef.child("user").child(user?.uid).updateChildren(childUpdates)
            Toast.makeText(
                this@Profile,"Daily Target Steps has been saved",Toast.LENGTH_LONG
            ).show()
        }
        btnLogout.setOnClickListener {
            mAuth.signOut()
            // this must be signed out to ensure the app prompts for google account in second try
            googleSignInClient.signOut()
            startActivity(Intent(this@Profile,Login::class.java))
        }
    }

    fun loadImageFromWebOperations(url: String?): Drawable? {
        try {
            val `is`: InputStream = URL(url).getContent() as InputStream
             return Drawable.createFromStream(`is`, "src name")
        } catch (e: Exception) {
            Log.d("IMAGEERROR",e.toString())
            return null
        }
    }
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
            bmImage.setImageBitmap(result)
        }


    }
}