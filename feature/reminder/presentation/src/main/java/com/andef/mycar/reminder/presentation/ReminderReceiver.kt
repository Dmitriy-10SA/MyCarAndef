package com.andef.mycar.reminder.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.andef.mycarandef.routes.Screen

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager

            createNotificationChannel(notificationManager)

            val id = intent?.extras?.getInt(ID_EXTRA) ?: 0

            val launchIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Screen.AllRemindersScreen.route.toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                it,
                id.toInt(),
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle("MyCar")
                .setSmallIcon(com.andef.mycarandef.design.R.drawable.my_car_car_key)
                .setContentText(intent?.extras?.getString(TEXT_EXTRA) ?: "")
                .setContentIntent(pendingIntent)
                .setColor(Color.WHITE)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(id.toInt(), notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Напоминания",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    companion object {
        private const val CHANNEL_ID = "reminder_channel_id"

        fun newIntent(context: Context, id: Int, text: String): Intent {
            return Intent(context, ReminderReceiver::class.java).apply {
                putExtra(ID_EXTRA, id)
                putExtra(TEXT_EXTRA, text)
            }
        }

        private const val ID_EXTRA = "id"
        private const val TEXT_EXTRA = "text"
    }
}