package com.andef.mycarandef.design.button.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.GreenColor

@Composable
fun UiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors()
    ) {
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 11.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors() = ButtonDefaults.buttonColors(
    containerColor = GreenColor,
    contentColor = White,
    disabledContainerColor = GreenColor.copy(alpha = 0.3f),
    disabledContentColor = White
)