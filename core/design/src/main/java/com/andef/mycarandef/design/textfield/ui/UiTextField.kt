package com.andef.mycarandef.design.textfield.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor

@Composable
fun UiTextField(
    isLightTheme: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String,
    leadingIcon: Painter,
    contentDescription: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = { Icon(painter = leadingIcon, contentDescription = contentDescription) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
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
    focusedBorderColor = grayColor(isLightTheme),
    unfocusedTextColor = grayColor(isLightTheme),
    unfocusedContainerColor = darkGrayOrWhiteColor(isLightTheme),
    unfocusedLabelColor = blackOrWhiteColor(isLightTheme),
    unfocusedPlaceholderColor = grayColor(isLightTheme),
    unfocusedLeadingIconColor = when (value.isEmpty()) {
        true -> grayColor(isLightTheme)
        else -> blackOrWhiteColor(isLightTheme)
    },
    cursorColor = blackOrWhiteColor(isLightTheme),
    unfocusedBorderColor = grayColor(isLightTheme),
    selectionColors = TextSelectionColors(
        handleColor = GreenColor,
        backgroundColor = GreenColor.copy(alpha = 0.2f)
    )
)