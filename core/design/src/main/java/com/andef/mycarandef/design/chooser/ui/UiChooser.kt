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
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor

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
        textStyle = TextStyle(color = blackOrWhiteColor(isLightTheme), fontSize = 16.sp)
    )
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors(value: String, isLightTheme: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = grayColor(isLightTheme),
    focusedContainerColor = darkGrayOrWhiteColor(isLightTheme),
    focusedLabelColor = blackOrWhiteColor(isLightTheme),
    focusedPlaceholderColor = grayColor(isLightTheme),
    focusedLeadingIconColor = when (value.isEmpty()) {
        true -> grayColor(isLightTheme)
        else -> blackOrWhiteColor(isLightTheme)
    },
    focusedTrailingIconColor = when (value.isEmpty()) {
        true -> grayColor(isLightTheme)
        else -> blackOrWhiteColor(isLightTheme)
    },
    focusedBorderColor = grayColor(isLightTheme),
    unfocusedTextColor = grayColor(isLightTheme),
    unfocusedContainerColor = darkGrayOrWhiteColor(isLightTheme),
    unfocusedLabelColor = blackOrWhiteColor(isLightTheme),
    unfocusedPlaceholderColor = grayColor(isLightTheme),
    unfocusedLeadingIconColor = when (value.isEmpty()) {
        true -> grayColor(isLightTheme)
        else -> blackOrWhiteColor(isLightTheme)
    },
    unfocusedTrailingIconColor = when (value.isEmpty()) {
        true -> grayColor(isLightTheme)
        else -> blackOrWhiteColor(isLightTheme)
    },
    unfocusedBorderColor = grayColor(isLightTheme)
)