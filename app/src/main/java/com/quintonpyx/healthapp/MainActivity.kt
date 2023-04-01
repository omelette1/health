package com.quintonpyx.healthapp


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.quintonpyx.healthapp.callback.PedometerCallback
import java.security.AccessController
import java.util.*

class MainActivity : AppCompatActivity(), PedometerCallback {

    private lateinit var database: DatabaseReference
    private lateinit var user: FirebaseUser
    private lateinit var txtUnit: TextView


    private var sensorManager: SensorManager? = null

    // variable gives the running status
    private var running = true

    // variable counts total steps
    private var totalSteps = 0f

    private lateinit var progressBar: CircularProgressBar

    val ACTIVITY_RECOGNITION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isPermissionGranted()) {
            requestPermission()
        }

        user = FirebaseAuth.getInstance().currentUser!!
        database = Firebase.database.reference
        txtUnit = findViewById(R.id.txtUnit)
        progressBar = findViewById(R.id.progress_bar)

        val eventListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val currentUser = snapshot.getValue(User::class.java) as User
                    txtUnit.setText("/ "+currentUser.targetSteps.toString()+" steps")
                    progressBar.progressMax = currentUser.targetSteps!!.toFloat()
//                    Log.d("MAX",currentUser.targetSteps!!.toString())
                } else {


                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"Error: "+error.toString(), Toast.LENGTH_LONG).show()
            }
        }

            // todo
        val snapshot = database.child("user").child("102140716770738350326")
            .addListenerForSingleValueEvent(eventListener)


//
//        //initializing sensorManager instance
//        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.steps

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food -> {
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.steps -> return@OnNavigationItemSelectedListener true
                R.id.leaderboard -> {
                    startActivity(Intent(this@MainActivity, Leaderboard::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.myFood -> {
                    startActivity(Intent(this@MainActivity, MyFood::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.profile -> {
                    startActivity(Intent(this@MainActivity, Profile::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

//

            }
            false
        })

//        running = true
//
//        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//
//        if (stepSensor == null) {
//            // show toast message, if there is no sensor in the device
//            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG).show()
//        } else {
//            // register listener with sensorManager
//            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
//        }
//
//        val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
//        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)
//
//        if (sharedPreferences.getString("date","") != GeneralHelper.getTodayDate()) {
//            val stepsShown = 0
//            tv_stepsTaken.text = ("$stepsShown")
//            progressBar.setProgressWithAnimation(stepsShown.toFloat(),1000)
//
//        }
//        createNotificationChannel()

         startService(Intent(this@MainActivity,PedometerService::class.java))
        PedometerService.subscribe.register(this@MainActivity)
        }

//    override fun onResume() {
//
//        super.onResume()
//        running = true
//
//        // TYPE_STEP_COUNTER:  A constant describing a step counter sensor
//        // Returns the number of steps taken by the user since the last reboot while activated
//        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
//        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//
//        if (stepSensor == null) {
//            // show toast message, if there is no sensor in the device
//            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG).show()
//        } else {
//            // register listener with sensorManager
//            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
//        }
//    }

//    override fun onPause() {
//        super.onPause()
//        running = false
//        // unregister listener
//        sensorManager?.unregisterListener(this)
//    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                ACTIVITY_RECOGNITION_REQUEST_CODE
            )
//            Log.d("HAHA","WRONG")

        }

    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }



    //handle requested permission result(allow or deny)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ACTIVITY_RECOGNITION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }
            }
        }


    }



    override fun subscribeSteps(steps: Int) {
        // get textview by its id
        // set current steps in textview

        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)


        tv_stepsTaken.text = ("$steps")

        progressBar.setProgressWithAnimation(steps.toFloat(),1000)

    }
}