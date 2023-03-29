package com.quintonpyx.healthapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators.and
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import java.util.*

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button

    private lateinit var btnSignUp: Button
    private lateinit var btnGoogleLogin: Button

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("545793299744-v54dj86gie9l22rjvbbldqv9koheosh5.apps.googleusercontent.com").requestEmail().build()
//          R.string not updated
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(R.string.default_web_client_id).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        // remove action bar
        supportActionBar?.hide()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)

        btnSignUp = findViewById(R.id.btn_signup)
        btnGoogleLogin = findViewById(R.id.btn_googleLogin)
        mAuth = FirebaseAuth.getInstance()

        // double colon means get class
        btnSignUp.setOnClickListener{
            // redirect
            val intent = Intent(this@Login,SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()
            if ((email.isEmpty()) and (password.isEmpty()== false)){
                Toast.makeText(this, "Please enter your email to login.", Toast.LENGTH_LONG).show()
            }else if ((password.isEmpty()) and (email.isEmpty()==false)){
                Toast.makeText(this, "Please enter your password to login.", Toast.LENGTH_LONG).show()
            }else if ((email.isEmpty()) and (password.isEmpty())){
                Toast.makeText(this, "Please enter your email and password to login.", Toast.LENGTH_LONG).show()
            }else {
                 login(email, password)
            }
        }

        btnGoogleLogin.setOnClickListener {

            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }



    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }

        Log.d("SIGNINRESULT",result.resultCode.toString())

    }

    private fun login(email:String,password:String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
                    val intent = Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                    Toast.makeText(this@Login,"Invalid email or password",Toast.LENGTH_LONG).show()
                }
            }
    }


    private fun handleSignInResult(task: Task<GoogleSignInAccount>){
        Log.d("SIGNINRESULT",task.isSuccessful.toString())
        if(task.isSuccessful()){
            val acc: GoogleSignInAccount? = task.result
            updateUI(acc!!)

        }
        else{
            Toast.makeText(this@Login,"Unexpected error occured",Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(acc:GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acc.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener{
                if(it.isSuccessful){
                    mDbRef = FirebaseDatabase.getInstance().getReference()

                    val eventListener = object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){

                            } else {
                                // User does not exist. NOW call createUserWithEmailAndPassword
                                mDbRef.child("user").child(acc.id!!).setValue(User(acc.displayName,acc.email,acc.id,0))

                                // Your previous code here.

                            }
                        }


                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@Login,"Error: "+error.toString(),Toast.LENGTH_LONG).show()
                        }
                    }
                    val snapshot = mDbRef.child("user").child(acc.id!!)
                        .addListenerForSingleValueEvent(eventListener)

                    val intent = Intent(this@Login,MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }else{
                    Toast.makeText(this@Login,it.exception.toString(),Toast.LENGTH_LONG).show()
                }
        }
    }


}
