package com.andef.mycarandef.design.auto.resize.text.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizeText(
    text: String,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    fontWeight: FontWeight? = null,
    maxFontSize: TextUnit = 16.sp,
    minFontSize: TextUnit = 2.sp,
    modifier: Modifier = Modifier
) {
    var textSize by remember { mutableStateOf(maxFontSize) }

    Text(
        fontWeight = fontWeight,
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { result ->
            if (result.hasVisualOverflow && textSize > minFontSize) {
                textSize = (textSize.value - 1).sp
            }
        }
    )
}