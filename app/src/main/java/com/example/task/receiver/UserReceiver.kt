package com.example.task.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.task.R

class UserReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == "com.kajal.USER_ADDED") {
            val builder = NotificationCompat.Builder(context, "user_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("User Added")
                .setContentText("New user(s) have been added.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val manager = ContextCompat.getSystemService(context, NotificationManager::class.java)
            manager?.notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}
