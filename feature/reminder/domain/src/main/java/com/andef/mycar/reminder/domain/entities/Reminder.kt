package com.andef.mycar.reminder.domain.entities

import java.time.LocalDate
import java.time.LocalTime

data class Reminder(
    val id: Long,
    val text: String,
    val date: LocalDate,
    val time: LocalTime,
    val carId: Long,
    val carName: String
)
