package com.andef.mycarandef.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RubleAmountVisualTransformation : VisualTransformation {
    private val dfs = DecimalFormatSymbols(Locale("ru", "RU")).apply {
        groupingSeparator = ' '
        decimalSeparator = '.'
    }
    private val formatter = DecimalFormat("#,##0.00", dfs)
    private val suffix = "₽"

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        if (raw.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }
        val normalized = raw
            .replace("\u00A0", "")
            .replace(" ", "")
            .replace(',', '.')
        val number = normalized.toDoubleOrNull()
        if (number == null) {
            return TransformedText(AnnotatedString(raw), OffsetMapping.Identity)
        }
        val formattedNumber = formatter.format(number)
        val showText = formattedNumber + suffix
        val offsetMapping = buildOffsetMappingForMoney(
            raw = normalized,
            formatted = formattedNumber,
            suffixLength = suffix.length,
            decimalChar = '.'
        )
        return TransformedText(AnnotatedString(showText), offsetMapping)
    }

    private fun buildOffsetMappingForMoney(
        raw: String,
        formatted: String,
        suffixLength: Int,
        decimalChar: Char
    ): OffsetMapping {
        val rawLen = raw.length
        val fmtLen = formatted.length
        val editableFmtIdx = buildList {
            formatted.forEachIndexed { idx, ch ->
                if (ch.isDigit() || ch == decimalChar) add(idx)
            }
        }
        val editableCount = editableFmtIdx.size
        val rawToFmt = IntArray(rawLen + 1)
        var taken = 0

        fun map(rawPos: Int, fmtPos: Int) {
            rawToFmt[rawPos] = fmtPos
        }

        for (i in 0 until rawLen) {
            val r = raw[i]
            if (!r.isDigit() && r != decimalChar) continue
            if (taken < editableCount) {
                map(i, editableFmtIdx[taken])
                taken++
            } else {
                map(i, editableFmtIdx.lastOrNull() ?: (fmtLen - suffixLength))
            }
        }
        rawToFmt[rawLen] = editableFmtIdx.lastOrNull()?.plus(1) ?: (fmtLen - suffixLength)

        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, rawLen)
                return rawToFmt[o].coerceAtMost(fmtLen - suffixLength)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val limited = offset.coerceIn(0, fmtLen - suffixLength)
                val pos = editableFmtIdx.binarySearch(limited).let { idx ->
                    if (idx >= 0) idx else (-idx - 2).coerceAtLeast(0)
                }
                val targetFmt = editableFmtIdx.getOrNull(pos) ?: return rawLen
                for (i in 0 until rawLen) {
                    if (rawToFmt[i] == targetFmt) return i
                }
                return rawLen
            }
        }
    }
}