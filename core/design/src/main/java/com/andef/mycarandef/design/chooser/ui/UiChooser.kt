package com.andef.mycarandef.design.chooser.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White

@Composable
fun UiChooser(
    onClick: () -> Unit,
    isLightTheme: Boolean,
    value: String,
    modifier: Modifier = Modifier,
    placeholderText: String,
    leadingIcon: Painter,
    trailingIcon: Painter,
    leadingIconContentDescription: String,
    trailingIconContentDescription: String
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = {},
        placeholder = { Text(text = placeholderText, fontSize = 16.sp) },
        leadingIcon = {
            Icon(
                painter = leadingIcon,
                contentDescription = leadingIconContentDescription
            )
        },
        trailingIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    painter = trailingIcon,
                    contentDescription = trailingIconContentDescription
                )
            }
        },
        singleLine = true,
        readOnly = true,
        shape = shape,
        colors = colors(value = value, isLightTheme = isLightTheme),
        textStyle = TextStyle(color = if (isLightTheme) Black else White, fontSize = 16.sp)
    )
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors(value: String, isLightTheme: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = if (isLightTheme) GrayForLight else GrayForDark,
    focusedContainerColor = if (isLightTheme) White else DarkGray,
    focusedLabelColor = if (isLightTheme) Black else White,
    focusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
    focusedLeadingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    focusedTrailingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    focusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedTextColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedContainerColor = if (isLightTheme) White else DarkGray,
    unfocusedLabelColor = if (isLightTheme) Black else White,
    unfocusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedLeadingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    unfocusedTrailingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    unfocusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark
)