package com.example.task

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.task.receiver.UserReceiver
import com.example.task.ui.screens.HomeScreen
import com.example.task.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {

    private lateinit var receiver: UserReceiver

    companion object {
        const val ACTION_USER_ADDED = "com.kajal.USER_ADDED"
        const val NOTIFICATION_CHANNEL_ID = "user_channel_id"
        const val NOTIFICATION_ID = 100
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showWelcomeNotification()
                Toast.makeText(this, " Welcome! Notifications are enabled.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiver = UserReceiver()

        createNotificationChannel()
        registerUserReceiver()
        requestNotificationPermissionIfNeeded()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            MyAppTheme(darkTheme = isDarkTheme) {
                HomeScreen()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "User Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "User activity notifications"
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerUserReceiver() {
        val filter = IntentFilter(ACTION_USER_ADDED)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(receiver, filter)
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showWelcomeNotification()
            }
        } else {
            showWelcomeNotification()
        }
    }

    private fun showWelcomeNotification() {
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Welcome")
            .setContentText("App launched successfully!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, builder.build())
    }
}
