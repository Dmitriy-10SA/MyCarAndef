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
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White

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
    focusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedTextColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedContainerColor = if (isLightTheme) White else DarkGray,
    unfocusedLabelColor = if (isLightTheme) Black else White,
    unfocusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedLeadingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    cursorColor = if (isLightTheme) Black else White,
    unfocusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark,
    selectionColors = TextSelectionColors(
        handleColor = Blue,
        backgroundColor = Blue.copy(alpha = 0.2f)
    )
)