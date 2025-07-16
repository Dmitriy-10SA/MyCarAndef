package com.andef.mycarandef.design.alertdialog.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiAlertDialog(
    isLightTheme: Boolean,
    isVisible: Boolean,
    title: String,
    onDismissRequest: () -> Unit,
    onYesClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textAlign = TextAlign.Center,
                    text = title,
                    fontSize = 22.sp
                )
            },
            containerColor = if (isLightTheme) White else DarkGray,
            titleContentColor = if (isLightTheme) Black else White,
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onCancelClick,
                        shape = textShape,
                        colors = textColors(isLightTheme),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isLightTheme) Black else White,
                                shape = textShape
                            )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp),
                            text = "Отмена",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.padding(12.dp))
                    TextButton(
                        onClick = onYesClick,
                        shape = textShape,
                        colors = textColors(isLightTheme),
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = if (isLightTheme) Black else White,
                                shape = textShape
                            )
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 8.dp),
                            text = "Да",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    }
                }
            },
            shape = shape
        )
    }
}

private val textShape = RoundedCornerShape(16.dp)
private val shape = RoundedCornerShape(28.dp)

@Composable
private fun textColors(isLightTheme: Boolean) = ButtonDefaults.textButtonColors(
    containerColor = Color.Transparent,
    contentColor = if (isLightTheme) Black else White,
    disabledContentColor = if (isLightTheme) Black.copy(alpha = 0.3f) else White.copy(alpha = 0.3f),
    disabledContainerColor = Color.Transparent
)