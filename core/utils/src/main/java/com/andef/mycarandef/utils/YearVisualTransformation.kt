package com.andef.mycarandef.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class YearVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val suffix = if (text.text.isNotEmpty()) "г" else ""
        val transformed = AnnotatedString(text.text + suffix)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset.coerceIn(0, text.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset.coerceIn(0, text.length)
            }
        }
        return TransformedText(transformed, offsetMapping)
    }
}