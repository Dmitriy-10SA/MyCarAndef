package com.andef.mycarandef.design.timepicker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.dialog.container.ui.UiDialogContainer
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTimePickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    onDismissRequest: () -> Unit,
    onOkClick: (LocalTime) -> Unit
) {
    if (isVisible) {
        val timePickerState = rememberTimePickerState(is24Hour = true)
        UiDialogContainer(isLightTheme, onDismissRequest) {
            Column {
                TimePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    state = timePickerState,
                    colors = timePickerColors(isLightTheme)
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = grayColor(isLightTheme)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    ActionButton(
                        modifier = Modifier.matchParentSize(),
                        onClick = {
                            onOkClick(
                                LocalTime.of(
                                    timePickerState.hour, timePickerState.minute
                                )
                            )
                        },
                        isLightTheme = isLightTheme,
                        text = "Сохранить",
                        color = GreenColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    text: String,
    color: Color
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = textButtonShape(topEnd = 0.dp, topStart = 0.dp),
        colors = textButtonColors(isLightTheme = isLightTheme)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun textButtonColors(isLightTheme: Boolean) = ButtonDefaults.textButtonColors(
    containerColor = Color.Transparent,
    contentColor = blackOrWhiteColor(isLightTheme),
    disabledContentColor = (blackOrWhiteColor(isLightTheme)).copy(alpha = 0.3f),
    disabledContainerColor = Color.Transparent
)

private fun textButtonShape(
    topStart: Dp = 16.dp,
    topEnd: Dp = 16.dp,
    bottomStart: Dp = 16.dp,
    bottomEnd: Dp = 16.dp
) = RoundedCornerShape(
    topEnd = topEnd,
    topStart = topStart,
    bottomEnd = bottomEnd,
    bottomStart = bottomStart
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun timePickerColors(isLightTheme: Boolean) = TimePickerDefaults.colors(
    clockDialColor = darkGrayOrWhiteColor(isLightTheme),
    selectorColor = GreenColor,
    periodSelectorBorderColor = blackOrWhiteColor(isLightTheme),
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    periodSelectorSelectedContainerColor = GreenColor,
    periodSelectorUnselectedContainerColor = Color.Transparent,
    periodSelectorSelectedContentColor = WhiteColor,
    periodSelectorUnselectedContentColor = blackOrWhiteColor(isLightTheme),
    timeSelectorSelectedContainerColor = GreenColor,
    clockDialSelectedContentColor = WhiteColor,
    clockDialUnselectedContentColor = blackOrWhiteColor(isLightTheme),
    timeSelectorUnselectedContainerColor = Color.Transparent,
    timeSelectorSelectedContentColor = WhiteColor,
    timeSelectorUnselectedContentColor = blackOrWhiteColor(isLightTheme)
)