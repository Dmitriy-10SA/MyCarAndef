package com.andef.mycarandef.utils

fun clampToTwoDecimals(input: String): String {
    val idx = input.indexOfFirst { it == '.' || it == ',' }
    return if (idx >= 0 && input.length > idx + 3) {
        input.substring(0, idx + 3)
    } else {
        input
    }
}