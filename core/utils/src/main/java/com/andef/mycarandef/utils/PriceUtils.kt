package com.andef.mycarandef.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun formatPriceRuble(value: Double): String {
    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    val formatter = DecimalFormat("#,##0.00", symbols)
    return "${formatter.format(value)}₽"
}