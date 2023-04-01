package com.quintonpyx.healthapp.helper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationCompat
import com.quintonpyx.healthapp.MainActivity
import com.quintonpyx.healthapp.R
import java.security.AccessController.getContext
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


        fun updateNotification(context: Context, service: Service, step: Int, targetSteps:Int){
            val NOTIFICATION_ID = 1301
            val CHANNEL_ID = "Reminder Notification Channel ID"

            var notiBuilder: Notification.Builder = Notification.Builder(context)
            var notiManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            var notification = NotificationCompat.Builder(context,CHANNEL_ID)
                .setContentTitle("Health App Step Counter")
                .setContentText(step.toString() + " / " + targetSteps.toString()+" steps")
                .setTicker(step.toString() + " / " + targetSteps.toString()+" steps")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setStyle(NotificationCompat.BigTextStyle().bigText("Step Counter"))
                .setStyle(NotificationCompat.BigTextStyle().bigText(step.toString() + " / " + targetSteps.toString()+" steps"))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setProgress(targetSteps, step, false)
                //.setProgress(this.dailyStepGoal, totalSteps, false)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(null)
                .setOngoing(true)
                .build();




            service.startForeground(NOTIFICATION_ID, notification)
            // Set Service to run in the Foreground
            notiManager.notify(NOTIFICATION_ID, notification)

        }
    }
}