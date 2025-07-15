package com.andef.mycarandef.design.datepicker.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiDatePickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    datePickerState: DatePickerState,
    onDismissRequest: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    onOkClick: (LocalDate) -> Unit,
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
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onOkClick(selectedDate)
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null,
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
            colors = colors(isLightTheme)
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.verticalScroll(rememberScrollState()),
                colors = colors(isLightTheme),
                dateFormatter = russianDateFormatter
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val russianDateFormatter = object : DatePickerFormatter {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale("ru"))
    private val monthYearFormatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))

    override fun formatDate(
        dateMillis: Long?,
        locale: CalendarLocale,
        forContentDescription: Boolean
    ): String? {
        if (dateMillis == null) return null
        val date = Instant.ofEpochMilli(dateMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date.format(dateFormatter)
    }

    override fun formatMonthYear(
        monthMillis: Long?,
        locale: CalendarLocale
    ): String? {
        if (monthMillis == null) return null
        val date = Instant.ofEpochMilli(monthMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date.format(monthYearFormatter)
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
private fun colors(isLightTheme: Boolean) = DatePickerDefaults.colors(
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