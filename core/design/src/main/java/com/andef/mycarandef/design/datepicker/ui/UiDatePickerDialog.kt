package com.andef.mycarandef.design.datepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.auto.resize.text.ui.AutoResizeText
import com.andef.mycarandef.design.dialog.container.ui.UiDialogContainer
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.utils.formatLocalDate
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiDatePickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    onDismissRequest: () -> Unit,
    onOkClick: (LocalDate) -> Unit
) {
    if (isVisible) {
        var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

        UiDialogContainer(isLightTheme = isLightTheme, onDismissRequest = onDismissRequest) {
            Column {
                Header(isLightTheme = isLightTheme, selectedDate = selectedDate)
                DaysRow(isLightTheme = isLightTheme)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = grayColor(isLightTheme)
                )
                Calendar(
                    isLightTheme = isLightTheme,
                    onDayClick = { date -> selectedDate = date },
                    selectedDate = selectedDate
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
                        enabled = selectedDate != null,
                        onClick = { selectedDate?.let { onOkClick(it) } },
                        isLightTheme = isLightTheme,
                        text = "Сохранить",
                        color = if (selectedDate != null) GreenColor else GreenColor.copy(alpha = 0.3f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    text: String,
    color: Color
) {
    TextButton(
        enabled = enabled,
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

@OptIn(ExperimentalTime::class)
@Composable
private fun Calendar(
    isLightTheme: Boolean,
    onDayClick: (LocalDate) -> Unit,
    selectedDate: LocalDate?,
) {
    val startDateForState = LocalDate.now().minusYears(2)
    val endDateForState = LocalDate.now().plusYears(2)
    VerticalCalendar(
        modifier = Modifier.height(300.dp),
        state = rememberCalendarState(
            firstVisibleMonth = YearMonth.now(),
            firstDayOfWeek = DayOfWeek.MONDAY,
            startMonth = YearMonth.of(startDateForState.year, startDateForState.month.value),
            endMonth = YearMonth.of(endDateForState.year, endDateForState.month.value),
            outDateStyle = OutDateStyle.EndOfRow
        ),
        monthHeader = { month ->
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = "${getMonthName(month.yearMonth.month)} ${month.yearMonth.year}",
                fontSize = 14.sp,
                color = blackOrWhiteColor(isLightTheme)
            )
            Spacer(modifier = Modifier.height(3.dp))
        },
        dayContent = { day ->
            DayText(
                isToday = day.date == LocalDate.now(),
                date = day.date,
                inMonth = when (day.position) {
                    DayPosition.MonthDate -> true
                    else -> false
                },
                onClick = onDayClick,
                isChoose = selectedDate?.let { day.date == selectedDate } ?: false,
                isLightTheme = isLightTheme,
            )
        }
    )
}

@Composable
private fun DayText(
    isToday: Boolean,
    isChoose: Boolean,
    inMonth: Boolean,
    onClick: (LocalDate) -> Unit,
    isLightTheme: Boolean,
    date: LocalDate
) {
    val backgroundShape = when {
        isChoose -> RoundedCornerShape(16.dp)
        else -> RoundedCornerShape(0.dp)
    }
    val modifier = when (isChoose && inMonth) {
        true -> Modifier
            .fillMaxWidth()
            .padding(vertical = 1.5.dp)
            .clip(backgroundShape)
            .clickable(onClick = { onClick(date) })
            .background(GreenColor)
            .padding(vertical = 4.5.dp)

        false -> Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = inMonth, onClick = { onClick(date) })
            .padding(vertical = 6.dp)
    }
    val textDecoration = when {
        isToday && !isChoose -> TextDecoration.Underline
        else -> null
    }
    val color = when {
        inMonth && isChoose -> WhiteColor
        inMonth -> blackOrWhiteColor(isLightTheme)
        else -> (grayColor(isLightTheme)).copy(alpha = 0.4f)
    }
    Text(
        modifier = modifier,
        fontSize = 14.sp,
        text = "${date.dayOfMonth}",
        textAlign = TextAlign.Center,
        textDecoration = textDecoration,
        color = color
    )
}

@Composable
private fun DaysRow(isLightTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = day,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp,
                color = blackOrWhiteColor(isLightTheme)
            )
        }
    }
}

private val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

private fun getMonthName(month: Month): String = when (month) {
    Month.JANUARY -> "Январь"
    Month.FEBRUARY -> "Февраль"
    Month.MARCH -> "Март"
    Month.APRIL -> "Апрель"
    Month.MAY -> "Май"
    Month.JUNE -> "Июнь"
    Month.JULY -> "Июль"
    Month.AUGUST -> "Август"
    Month.SEPTEMBER -> "Сентябрь"
    Month.OCTOBER -> "Октябрь"
    Month.NOVEMBER -> "Ноябрь"
    Month.DECEMBER -> "Декабрь"
}

@Composable
private fun Header(isLightTheme: Boolean, selectedDate: LocalDate?) {
    AutoResizeText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp)
            .padding(horizontal = 6.dp),
        text = selectedDate?.let { s ->
            formatLocalDate(s)
        } ?: "Выбор даты",
        color = blackOrWhiteColor(isLightTheme),
        maxFontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}