package com.quintonpyx.healthapp.helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GeneralHelper {
    companion object{
        fun getTodayDate(): String{
            val date: Date = Calendar.getInstance().time
            val df: DateFormat = SimpleDateFormat("dd MMM yyyy")
            return df.format(date)
        }


        fun getCalories(steps: Int): String? {
            val Cal = (steps * 0.045).toInt()
            return "$Cal calories"
        }


        fun getDistanceCovered(steps: Int): String? {
            val feet = (steps * 2.5).toInt()
            val distance = feet/3.281
            val finalDistance:Double = String.format("%.2f", distance).toDouble()
            return "$finalDistance meter"
        }
    }
}