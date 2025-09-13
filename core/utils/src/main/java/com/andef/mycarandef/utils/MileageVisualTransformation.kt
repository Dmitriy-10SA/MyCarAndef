package com.andef.mycarandef.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MileageVisualTransformation : VisualTransformation {
    private val symbols = DecimalFormatSymbols(Locale.getDefault()).apply { groupingSeparator = ' ' }
    private val formatter = DecimalFormat("#,###", symbols)

    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }
        val number = original.toLongOrNull() ?: 0L
        val formatted = formatter.format(number)
        val transformedText = "${formatted}км"

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val coreLen = formatted.length
                val mapped = mapOriginalToFormatted(offset, original.length, formatted)
                return mapped.coerceAtMost(coreLen)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val coreLen = formatted.length
                val clamped = offset.coerceAtMost(coreLen)
                return mapFormattedToOriginal(clamped, formatted, original.length)
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }

    private fun mapOriginalToFormatted(
        originalOffset: Int,
        originalLen: Int,
        formatted: String
    ): Int {
        if (originalOffset <= 0) return 0
        if (originalOffset >= originalLen) return formatted.length
        var digitsSeen = 0
        formatted.forEachIndexed { i, ch ->
            if (ch.isDigit()) {
                digitsSeen++
                if (digitsSeen == originalOffset) {
                    return i + 1
                }
            }
        }
        return formatted.length
    }

    private fun mapFormattedToOriginal(
        formattedOffset: Int,
        formatted: String,
        originalLen: Int
    ): Int {
        if (formattedOffset <= 0) return 0
        var digitsSeen = 0
        formatted.forEachIndexed { i, ch ->
            if (i >= formattedOffset) return digitsSeen
            if (ch.isDigit()) digitsSeen++
        }
        return digitsSeen.coerceAtMost(originalLen)
    }
}