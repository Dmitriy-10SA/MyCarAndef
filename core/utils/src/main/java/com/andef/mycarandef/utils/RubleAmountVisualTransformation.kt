package com.andef.mycarandef.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class RubleAmountVisualTransformation : VisualTransformation {

    private val locale = Locale("ru", "RU")
    private val dfs = DecimalFormatSymbols(locale).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }
    private val formatter = DecimalFormat("#,##0.00", dfs)
    private val suffix = "₽"

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text

        // --- ПУСТО: ничего не отображаем сверх ввода, без суффикса, identity mapping.
        if (raw.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Пытаемся распарсить (разрешаем и ',' и '.').
        val number = raw.replace(',', '.').toDoubleOrNull()

        // --- НЕВАЛИДНО: показываем как есть, без суффикса, identity mapping.
        if (number == null) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // --- ВАЛИДНО: форматируем и добавляем суффикс.
        val formattedNumber = formatter.format(number) // пример: "1.234,56"
        val showText = formattedNumber + suffix

        val offsetMapping = buildOffsetMappingForMoney(
            raw = raw,
            formatted = formattedNumber,
            suffixLength = suffix.length
        )

        return TransformedText(AnnotatedString(showText), offsetMapping)
    }

    /**
     * Создаёт OffsetMapping между "сырым" вводом и форматированной строкой + суффикс.
     * Редактируемые символы в форматированной строке: цифры и ','.
     * Суффикс в редактирование не попадает.
     */
    private fun buildOffsetMappingForMoney(
        raw: String,
        formatted: String,
        suffixLength: Int
    ): OffsetMapping {
        val rawLen = raw.length
        val fmtLen = formatted.length

        // Индексы редактируемых символов в форматированной строке (цифры + десятичный разделитель).
        val editableFmtIdx = buildList {
            formatted.forEachIndexed { idx, ch ->
                if (ch.isDigit() || ch == ',') add(idx)
            }
        }
        val editableCount = editableFmtIdx.size

        // Для каждой позиции raw -> индекс редактируемого символа formatted.
        val rawToFmt = IntArray(rawLen + 1)
        var take = 0

        fun mapPos(rawPos: Int, fmtPos: Int) {
            rawToFmt[rawPos] = fmtPos
        }

        for (i in 0 until rawLen) {
            val r = raw[i]
            if (!r.isDigit() && r != ',' && r != '.') continue
            if (take < editableCount) {
                mapPos(i, editableFmtIdx[take])
                take++
            } else {
                // если raw длиннее, шлём в последний редактируемый символ
                mapPos(i, editableFmtIdx.lastOrNull() ?: (fmtLen - suffixLength))
            }
        }
        // позиция "в конец raw" -> после последнего редактируемого символа
        rawToFmt[rawLen] = editableFmtIdx.lastOrNull()?.plus(1) ?: (fmtLen - suffixLength)

        return object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, rawLen)
                val mapped = rawToFmt[o].coerceAtMost(fmtLen - suffixLength)
                return mapped
            }

            override fun transformedToOriginal(offset: Int): Int {
                val limited = offset.coerceIn(0, fmtLen - suffixLength) // не заходим в суффикс
                // ищем ближайший editableFmtIdx <= limited
                val pos = editableFmtIdx.binarySearch(limited).let { idx ->
                    if (idx >= 0) idx else (-idx - 2).coerceAtLeast(0)
                }
                // находим raw, соответствующий этому editableFmtIdx
                val targetFmt = editableFmtIdx.getOrNull(pos)
                if (targetFmt == null) return rawLen
                for (i in 0 until rawLen) {
                    if (rawToFmt[i] == targetFmt) return i
                }
                return rawLen
            }
        }
    }
}