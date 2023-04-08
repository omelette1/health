package com.quintonpyx.healthapp

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.quintonpyx.healthapp.callback.PedometerCallback
import com.quintonpyx.healthapp.helper.GeneralHelper
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*

class PedometerService : Service(), SensorEventListener {

    companion object{
        lateinit var callback: PedometerCallback
    }

    object subscribe {
        fun register(activity: Activity) {
            callback = activity as PedometerCallback
        }
    }


    private var running = true
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference
    private var totalSteps = 0f
    private var targetSteps = 10000



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        database = Firebase.database.reference

        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val countSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        user = FirebaseAuth.getInstance().currentUser!!


        val eventListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(usr in snapshot.children){
                        val currentUser = usr.getValue(User::class.java) as User
                        targetSteps = currentUser.targetSteps!!
//                        Log.d("MAX",currentUser.targetSteps!!.toString())
                    }

                } else {


                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext,"Error: "+error.toString(), Toast.LENGTH_LONG).show()
            }
        }

        val snapshot = database.child("user").orderByChild("uid").equalTo(user.uid)
                .addListenerForSingleValueEvent(eventListener)

        val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)

        if(countSensor != null){
//            Toast.makeText(this, "Step Detecting Start", Toast.LENGTH_SHORT).show()
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL)

            val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)


            if(sharedPreferences.getString("date","") != GeneralHelper.getTodayDate()||sharedPreferences.getString("userUid","")!=user.uid){
                GeneralHelper.updateNotification(this, this, sharedPreferences.getInt("finalSteps",0),targetSteps)
                callback?.subscribeSteps(sharedPreferences.getInt("finalSteps",0))

            }else{
                GeneralHelper.updateNotification(this, this, 0,targetSteps)
                callback?.subscribeSteps(0)

            }


        }else{
            Toast.makeText(this, "Sensor Not Detected", Toast.LENGTH_SHORT).show()
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onSensorChanged(event: SensorEvent?) {


//        Log.d("HAHaha","sensor is working")

        if (running) {

            //get the number of steps taken by the user.
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt()


            val sharedPreferences = getSharedPreferences("myPrefs",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (sharedPreferences.getString("date","") != GeneralHelper.getTodayDate()) {
                if(sharedPreferences.getString("userUid","")!=user.uid){
                    editor.putString("userUid", user.uid.toString())

                    editor.putInt("steps", currentSteps)
//                Log.d("STEPSSAVED",currentSteps.toString())
                    editor.putString("date", GeneralHelper.getTodayDate())
                    editor.putInt("finalSteps", 0)

                    editor.commit()
                }else{

                    editor.putInt("steps", currentSteps)
//                Log.d("STEPSSAVED",currentSteps.toString())
                    editor.putString("date", GeneralHelper.getTodayDate())
                    editor.putInt("finalSteps", 0)
                    editor.commit()

                }


            } else {

                if(sharedPreferences.getString("userUid","")!=user.uid) {
                    editor.putString("userUid", user.uid.toString())

                    editor.putInt("steps", currentSteps)
//                Log.d("STEPSSAVED",currentSteps.toString())
                    editor.putString("date", GeneralHelper.getTodayDate())
                    editor.putInt("finalSteps", 0)

                    editor.commit()
                }else{
                    val storeSteps = sharedPreferences.getInt("steps",0)
                    val sensorSteps = currentSteps
                    val finalSteps = sensorSteps - storeSteps

                    if (finalSteps >= 0) {
                        editor.putInt("finalSteps", finalSteps)
                        editor.commit()
                    }
                }




//                Log.d("FINALSTEP",sharedPreferences.getInt("steps",0).toString())
//                Log.d("DATE", sharedPreferences.getString("date",GeneralHelper.getTodayDate())!!)

            }
//            editor.commit()


            GeneralHelper.updateNotification(this, this, sharedPreferences.getInt("finalSteps",0),targetSteps)
            callback?.subscribeSteps(sharedPreferences.getInt("finalSteps",0))
            // add to previous day if it is a new day
                val eventListener = object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(usr in snapshot.children){
                                val currentUser = usr.getValue(User::class.java) as User
                                val previousSteps = currentUser.yesterdaySteps!!
                                if(currentUser.lastUpdated!=GeneralHelper.getTodayDate()){
                                    saveDataToFirebase(user,sharedPreferences.getInt("finalSteps",0)+previousSteps,true,currentUser.steps!!)

                                }else{
                                    saveDataToFirebase(user,sharedPreferences.getInt("finalSteps",0)+previousSteps,false)

                                }

                            }

                        } else {


                        }
                    }


                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(baseContext,"Error: "+error.toString(), Toast.LENGTH_LONG).show()
                    }
                }

                val snapshot = database.child("user").orderByChild("uid").equalTo(user.uid)
                    .addListenerForSingleValueEvent(eventListener)


        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        DriverManager.println("onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }


    private fun saveDataToFirebase(user: FirebaseUser, currentSteps:Int, newDayInd:Boolean,resetSteps:Int=0){

        if(newDayInd){
            val childUpdates= HashMap<String,Any>()
            childUpdates.put("steps",currentSteps)
            childUpdates.put("yesterdaySteps",resetSteps)
            childUpdates.put("lastUpdated",GeneralHelper.getTodayDate())
            database.child("user").child(user.uid).updateChildren(childUpdates)
        }else{
            val childUpdates= HashMap<String,Any>()
            childUpdates.put("steps",currentSteps)
            database.child("user").child(user.uid).updateChildren(childUpdates)
        }

    }








}