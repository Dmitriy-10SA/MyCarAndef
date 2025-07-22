package com.andef.mycarandef.design.timepicker.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.Red
import com.andef.mycarandef.design.theme.White
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTimePickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    timePickerState: TimePickerState,
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOkClick: (LocalTime) -> Unit,
    tonalElevation: Dp = DatePickerDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false)
) {
    if (isVisible) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            shape = shape,
            tonalElevation = tonalElevation,
            properties = properties,
            modifier = modifier,
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedTime =
                            LocalTime.of(timePickerState.hour, timePickerState.minute)
                        onOkClick(selectedTime)
                    },
                    shape = textShape,
                    colors = textColors(isLightTheme = isLightTheme)
                ) {
                    Text(text = "ОК", fontSize = 14.sp)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancelClick,
                    shape = textShape,
                    colors = textColors(isLightTheme = isLightTheme)
                ) {
                    Text(text = "Отмена", fontSize = 14.sp)
                }
            },
            colors = dialogColors(isLightTheme)
        ) {
            TimePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                state = timePickerState,
                colors = timePickerColors(isLightTheme)
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun timePickerColors(isLightTheme: Boolean) = TimePickerDefaults.colors(
    clockDialColor = if (isLightTheme) White else DarkGray,
    selectorColor = Blue,
    periodSelectorBorderColor = if (isLightTheme) Black else White,
    containerColor = if (isLightTheme) White else DarkGray,
    periodSelectorSelectedContainerColor = Blue,
    periodSelectorUnselectedContainerColor = Color.Transparent,
    periodSelectorSelectedContentColor = White,
    periodSelectorUnselectedContentColor = if (isLightTheme) Black else White,
    timeSelectorSelectedContainerColor = Blue,
    clockDialSelectedContentColor = White,
    clockDialUnselectedContentColor = if (isLightTheme) Black else White,
    timeSelectorUnselectedContainerColor = Color.Transparent,
    timeSelectorSelectedContentColor = White,
    timeSelectorUnselectedContentColor = if (isLightTheme) Black else White
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun dialogColors(isLightTheme: Boolean) = DatePickerDefaults.colors(
    containerColor = if (isLightTheme) White else DarkGray,
    titleContentColor = if (isLightTheme) Black else White,
    headlineContentColor = if (isLightTheme) Black else White,
    weekdayContentColor = if (isLightTheme) Black else White,
    subheadContentColor = if (isLightTheme) Black else White,
    navigationContentColor = if (isLightTheme) Black else White,
    yearContentColor = if (isLightTheme) Black else White,
    disabledYearContentColor = if (isLightTheme) Black else White,
    currentYearContentColor = if (isLightTheme) Black else White,
    selectedYearContentColor = if (isLightTheme) Black else White,
    disabledDayContentColor = if (isLightTheme) Black else White,
    disabledSelectedYearContainerColor = if (isLightTheme) Black else White,
    disabledSelectedDayContainerColor = if (isLightTheme) White else DarkGray,
    disabledSelectedYearContentColor = if (isLightTheme) Black else White,
    selectedDayContentColor = White,
    selectedDayContainerColor = Blue,
    selectedYearContainerColor = if (isLightTheme) Black else White,
    todayContentColor = if (isLightTheme) Black else White,
    todayDateBorderColor = if (isLightTheme) Black else White,
    dayContentColor = if (isLightTheme) Black else White,
    disabledSelectedDayContentColor = if (isLightTheme) Black else White,
    dayInSelectionRangeContainerColor = Blue,
    dayInSelectionRangeContentColor = White,
    dividerColor = Color.Transparent,
    dateTextFieldColors = TextFieldDefaults.colors(
        focusedTextColor = if (isLightTheme) Black else White,
        focusedContainerColor = if (isLightTheme) White else DarkGray,
        focusedLabelColor = if (isLightTheme) Black else White,
        focusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
        unfocusedTextColor = if (isLightTheme) Black else White,
        unfocusedContainerColor = if (isLightTheme) White else DarkGray,
        unfocusedLabelColor = if (isLightTheme) Black else White,
        unfocusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
        cursorColor = if (isLightTheme) Black else White,
        selectionColors = TextSelectionColors(
            handleColor = Blue,
            backgroundColor = Blue.copy(alpha = 0.2f)
        ),
        focusedIndicatorColor = if (isLightTheme) GrayForLight else GrayForDark,
        unfocusedIndicatorColor = if (isLightTheme) GrayForLight else GrayForDark,
        focusedSupportingTextColor = if (isLightTheme) GrayForLight else GrayForDark,
        unfocusedSupportingTextColor = if (isLightTheme) GrayForLight else GrayForDark,
        errorTextColor = if (isLightTheme) Black else White,
        errorLabelColor = if (isLightTheme) Black else White,
        errorCursorColor = if (isLightTheme) Black else White,
        errorContainerColor = if (isLightTheme) White else DarkGray,
        errorIndicatorColor = if (isLightTheme) GrayForLight else GrayForDark,
        errorPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
        errorSupportingTextColor = Red
    )
)