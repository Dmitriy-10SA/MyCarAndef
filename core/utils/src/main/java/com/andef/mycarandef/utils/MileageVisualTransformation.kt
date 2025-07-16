package com.andef.mycarandef.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MileageVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val original = text.text
        if (original.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }
        val transformedText = "${original}км"
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset.coerceAtMost(original.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return offset.coerceAtMost(original.length)
            }
        }
        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}