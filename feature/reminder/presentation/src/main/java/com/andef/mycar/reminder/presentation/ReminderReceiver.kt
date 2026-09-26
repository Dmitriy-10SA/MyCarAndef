package com.andef.mycar.reminder.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.app.AlarmManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.andef.mycar.reminder.domain.entities.ReminderRepeatType
import com.andef.mycar.reminder.domain.utils.nextOccurrenceDate
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager

            createNotificationChannel(notificationManager)

            val id = intent?.extras?.getInt(ID_EXTRA) ?: 0

            val launchIntent = it.packageManager.getLaunchIntentForPackage(it.packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent.getActivity(
                it,
                id.toInt(),
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle("Мои авто")
                .setSmallIcon(com.andef.mycarandef.design.R.drawable.my_car_car_key)
                .setContentText(intent?.extras?.getString(TEXT_EXTRA) ?: "")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(intent?.extras?.getString(TEXT_EXTRA) ?: "")
                )
                .setContentIntent(pendingIntent)
                .setColor(Color.WHITE)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(id.toInt(), notification)
            scheduleNextReminder(it, intent)
        }
    }

    private fun scheduleNextReminder(context: Context, intent: Intent?) {
        val repeatType = intent?.getStringExtra(REPEAT_TYPE_EXTRA)?.let { title ->
            ReminderRepeatType.entries.find { it.title == title }
        } ?: return
        val startDate = LocalDate.of(
            intent.getIntExtra(START_YEAR_EXTRA, 1970),
            intent.getIntExtra(START_MONTH_EXTRA, 1),
            intent.getIntExtra(START_DAY_EXTRA, 1)
        )
        val time = LocalTime.of(
            intent.getIntExtra(HOUR_EXTRA, 0),
            intent.getIntExtra(MINUTE_EXTRA, 0)
        )
        val zoneId = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zoneId)
        val fromDate = if (time > now.toLocalTime()) {
            now.toLocalDate()
        } else {
            now.toLocalDate().plusDays(1)
        }
        val triggerDate = nextOccurrenceDate(startDate, repeatType, fromDate) ?: return
        val id = intent.getIntExtra(ID_EXTRA, 0)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            newIntent(
                context = context,
                id = id,
                text = intent.getStringExtra(TEXT_EXTRA).orEmpty(),
                repeatType = repeatType,
                startDate = startDate,
                hour = time.hour,
                minute = time.minute
            ),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerDate.atTime(time).atZone(zoneId).toInstant().toEpochMilli(),
            pendingIntent
        )
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

        fun newIntent(
            context: Context,
            id: Int,
            text: String,
            repeatType: ReminderRepeatType? = null,
            startDate: LocalDate = LocalDate.of(1970, 1, 1),
            hour: Int = 0,
            minute: Int = 0
        ): Intent {
            return Intent(context, ReminderReceiver::class.java).apply {
                putExtra(ID_EXTRA, id)
                putExtra(TEXT_EXTRA, text)
                putExtra(REPEAT_TYPE_EXTRA, repeatType?.title)
                putExtra(START_YEAR_EXTRA, startDate.year)
                putExtra(START_MONTH_EXTRA, startDate.monthValue)
                putExtra(START_DAY_EXTRA, startDate.dayOfMonth)
                putExtra(HOUR_EXTRA, hour)
                putExtra(MINUTE_EXTRA, minute)
            }
        }

        private const val ID_EXTRA = "id"
        private const val TEXT_EXTRA = "text"
        private const val REPEAT_TYPE_EXTRA = "repeat_type"
        private const val START_YEAR_EXTRA = "start_year"
        private const val START_MONTH_EXTRA = "start_month"
        private const val START_DAY_EXTRA = "start_day"
        private const val HOUR_EXTRA = "hour"
        private const val MINUTE_EXTRA = "minute"
    }
}
