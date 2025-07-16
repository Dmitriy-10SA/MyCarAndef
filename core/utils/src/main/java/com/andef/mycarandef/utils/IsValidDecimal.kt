package com.andef.mycarandef.utils

fun String.isValidDecimal(maxDecimals: Int = 2): Boolean {
    val regex = Regex("^\\d{0,9}([.,]\\d{0,$maxDecimals})?$")
    return matches(regex)
}