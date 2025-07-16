package com.andef.mycarandef.utils

import java.util.Locale

fun formatAmountForEdit(value: Double): String {
    return String.format(Locale("ru", "RU"), "%.2f", value).replace('.', ',')
}