package com.andef.mycarandef.utils

import java.time.LocalTime

fun LocalTime.toInt() = this.toSecondOfDay()

fun Int.toLocalTime() = LocalTime.ofSecondOfDay(this.toLong())