package com.quintonpyx.healthapp


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.sql.DriverManager.println
import android.content.Intent

import androidx.annotation.NonNull

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.quintonpyx.healthapp.helper.GeneralHelper


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null

    // variable gives the running status
    private var running = false

    // variable counts total steps
    private var totalSteps = 0f

    val ACTIVITY_RECOGNITION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //check if permission isn't already granted, request the permission
        if (isPermissionGranted()) {
            requestPermission()
        }

        //initializing sensorManager instance
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // menu code
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.home

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.food -> {
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                    // override default transition from page to page
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.steps -> return@OnNavigationItemSelectedListener false

            }
            false
        })
    }

    override fun onResume() {

        super.onResume()
        running = true

        // TYPE_STEP_COUNTER:  A constant describing a step counter sensor
        // Returns the number of steps taken by the user since the last reboot while activated
        // This sensor requires permission android.permission.ACTIVITY_RECOGNITION.
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            // show toast message, if there is no sensor in the device
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            // register listener with sensorManager
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        // unregister listener
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {


//        Log.d("HAHaha","sensor is working")
        // get textview by its id
        var tv_stepsTaken = findViewById<TextView>(R.id.tv_stepsTaken)

        if (running) {

            //get the number of steps taken by the user.
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt()

            val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (sharedPreferences.getString("date",GeneralHelper.getTodayDate()) != GeneralHelper.getTodayDate()) {
                editor.putInt("steps", currentSteps)
                editor.putString("date", GeneralHelper.getTodayDate())
            } else {
                val storeSteps = sharedPreferences.getInt("steps",0)
                val sensorSteps = currentSteps
                val finalSteps = sensorSteps - storeSteps

                if (finalSteps > 0) {
                    editor.putInt("finalSteps", finalSteps)
                }


                Log.d("FINALSTEP",sharedPreferences.getInt("finalSteps",0).toString())
                Log.d("DATE", sharedPreferences.getString("date",GeneralHelper.getTodayDate())!!)

            }
            editor.commit()

//            saveData(currentSteps)
            // set current steps in textview
            val stepsShown = sharedPreferences.getInt("finalSteps",0)
            tv_stepsTaken.text = ("$stepsShown")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        println("onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    private fun saveData(currentSteps:Int){
        //todo:save to firebase
    }

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
}