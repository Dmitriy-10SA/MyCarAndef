package com.andef.mycarandef.work.domain.entities

import java.time.LocalDate

data class Work(
    val id: Long,
    val title: String,
    val note: String?,
    val mileage: Int,
    val date: LocalDate,
    val carId: Long
)
