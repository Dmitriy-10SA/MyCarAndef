package com.andef.mycar.reminder.data.mapper

import com.andef.mycar.reminder.data.dbo.ReminderDbo
import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycarandef.utils.toInt
import com.andef.mycarandef.utils.toLocalDate
import com.andef.mycarandef.utils.toLocalTime
import javax.inject.Inject

class ReminderMapper @Inject constructor() {
    fun map(reminder: Reminder): ReminderDbo = ReminderDbo(
        id = reminder.id,
        text = reminder.text,
        date = reminder.date.toInt(),
        time = reminder.time.toInt(),
        carId = reminder.carId,
        carName = reminder.carName
    )

    fun map(reminderDbo: ReminderDbo): Reminder = Reminder(
        id = reminderDbo.id,
        text = reminderDbo.text,
        date = reminderDbo.date.toLocalDate(),
        time = reminderDbo.time.toLocalTime(),
        carId = reminderDbo.carId,
        carName = reminderDbo.carName
    )
}