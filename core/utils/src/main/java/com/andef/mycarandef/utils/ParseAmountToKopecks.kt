package com.andef.mycarandef.utils

fun parseAmountToKopecks(input: String): Long? {
    val normalized = input
        .replace("\u00A0", "")
        .replace(" ", "")
        .replace(',', '.')

    if (normalized.isBlank()) return null

    val parts = normalized.split('.', limit = 2)
    val rubles = parts[0].ifBlank { "0" }
    if (!rubles.all { it.isDigit() }) return null

    val kopecks = parts.getOrNull(1).orEmpty()
    if (!kopecks.all { it.isDigit() }) return null

    val rublesValue = rubles.toLongOrNull() ?: return null
    val kopecksValue = kopecks
        .padEnd(2, '0')
        .take(2)
        .ifBlank { "0" }
        .toLongOrNull()
        ?: return null

    if (rublesValue > (Long.MAX_VALUE - kopecksValue) / 100) return null

    return rublesValue * 100 + kopecksValue
}
