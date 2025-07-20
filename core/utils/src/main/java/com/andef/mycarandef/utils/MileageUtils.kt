package com.andef.mycarandef.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatMileage(mileage: Int): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply { groupingSeparator = ' ' }
    val formatter = DecimalFormat("#,###", symbols)
    return "${formatter.format(mileage)}км"
}