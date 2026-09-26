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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.R
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

@Composable
fun UiReminderDatePickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    initialSelectedDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
    reminderDates: Set<LocalDate>,
    onDismissRequest: () -> Unit,
    onOkClick: (LocalDate) -> Unit
) {
    if (isVisible) {
        val today = remember { LocalDate.now() }
        var selectedDate by remember(initialSelectedDate, startDate, endDate) {
            mutableStateOf(initialSelectedDate.coerceIn(startDate, endDate))
        }
        var scrollToSelectedRequest by remember { mutableIntStateOf(0) }

        UiDialogContainer(isLightTheme = isLightTheme, onDismissRequest = onDismissRequest) {
            Column {
                ReminderHeader(
                    isLightTheme = isLightTheme,
                    selectedDate = selectedDate,
                    onTodayClick = {
                        selectedDate = today.coerceIn(startDate, endDate)
                        scrollToSelectedRequest++
                    }
                )
                ReminderDaysRow(isLightTheme = isLightTheme)
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = grayColor(isLightTheme)
                )
                ReminderCalendar(
                    isLightTheme = isLightTheme,
                    selectedDate = selectedDate,
                    startDate = startDate,
                    endDate = endDate,
                    reminderDates = reminderDates,
                    scrollToSelectedRequest = scrollToSelectedRequest,
                    onDayClick = { selectedDate = it }
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 0.5.dp,
                    color = grayColor(isLightTheme)
                )
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = { onOkClick(selectedDate) },
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = blackOrWhiteColor(isLightTheme)
                    )
                ) {
                    Text(
                        text = "Показать",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderHeader(
    isLightTheme: Boolean,
    selectedDate: LocalDate,
    onTodayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 12.dp)
    ) {
        AutoResizeText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .align(Alignment.Center),
            text = formatLocalDate(selectedDate),
            color = blackOrWhiteColor(isLightTheme),
            maxFontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        IconButton(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterEnd),
            onClick = onTodayClick
        ) {
            Icon(
                modifier = Modifier.size(22.dp),
                painter = painterResource(R.drawable.my_car_today),
                tint = blackOrWhiteColor(isLightTheme),
                contentDescription = "Перейти к сегодняшней дате"
            )
        }
    }
}

@Composable
private fun ReminderCalendar(
    isLightTheme: Boolean,
    selectedDate: LocalDate,
    startDate: LocalDate,
    endDate: LocalDate,
    reminderDates: Set<LocalDate>,
    scrollToSelectedRequest: Int,
    onDayClick: (LocalDate) -> Unit
) {
    val firstVisibleDate = selectedDate.coerceIn(startDate, endDate)
    val calendarState = rememberCalendarState(
        firstVisibleMonth = YearMonth.from(firstVisibleDate),
        firstDayOfWeek = DayOfWeek.MONDAY,
        startMonth = YearMonth.from(startDate),
        endMonth = YearMonth.from(endDate),
        outDateStyle = OutDateStyle.EndOfRow
    )
    LaunchedEffect(selectedDate, scrollToSelectedRequest) {
        calendarState.animateScrollToMonth(YearMonth.from(selectedDate))
    }
    VerticalCalendar(
        modifier = Modifier.height(300.dp),
        state = calendarState,
        monthHeader = { month ->
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                text = "${reminderMonthName(month.yearMonth.month)} ${month.yearMonth.year}",
                color = blackOrWhiteColor(isLightTheme),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
        },
        dayContent = { day ->
            val isInRange = day.date in startDate..endDate
            val isInMonth = day.position == DayPosition.MonthDate
            ReminderDay(
                date = day.date,
                isEnabled = isInMonth && isInRange,
                isSelected = day.date == selectedDate && isInRange,
                hasReminder = isInMonth && isInRange && reminderDates.contains(day.date),
                isLightTheme = isLightTheme,
                onClick = onDayClick
            )
        }
    )
}

@Composable
private fun ReminderDay(
    date: LocalDate,
    isEnabled: Boolean,
    isSelected: Boolean,
    hasReminder: Boolean,
    isLightTheme: Boolean,
    onClick: (LocalDate) -> Unit
) {
    val dayModifier = if (isSelected && isEnabled) {
        Modifier
            .fillMaxWidth()
            .padding(vertical = 1.5.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(date) }
            .background(GreenColor)
            .padding(vertical = 4.5.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = isEnabled) { onClick(date) }
            .padding(vertical = 6.dp)
    }
    val dayColor = when {
        isSelected && isEnabled -> WhiteColor
        isEnabled -> blackOrWhiteColor(isLightTheme)
        else -> grayColor(isLightTheme).copy(alpha = 0.4f)
    }
    val isToday = isEnabled && date == LocalDate.now()
    val todayIndicatorColor = blackOrWhiteColor(isLightTheme)
    val indicatorModifier = if ((isToday && !isSelected) || hasReminder) {
        Modifier.drawBehind {
            if (hasReminder) {
                drawCircle(
                    color = if (isSelected) WhiteColor else GreenColor,
                    radius = 2.5.dp.toPx(),
                    center = Offset(
                        x = size.width / 2f - if (isToday && !isSelected) 4.dp.toPx() else 0f,
                        y = size.height - 2.dp.toPx()
                    )
                )
            }
            if (isToday && !isSelected) {
                drawCircle(
                    color = todayIndicatorColor,
                    radius = 2.5.dp.toPx(),
                    center = Offset(
                        x = size.width / 2f + if (hasReminder) 4.dp.toPx() else 0f,
                        y = size.height - 2.dp.toPx()
                    )
                )
            }
        }
    } else {
        Modifier
    }

    Text(
        modifier = dayModifier.then(indicatorModifier),
        text = date.dayOfMonth.toString(),
        color = dayColor,
        fontSize = 14.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ReminderDaysRow(isLightTheme: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        reminderWeekDays.forEach { day ->
            Text(
                modifier = Modifier.weight(1f),
                text = day,
                color = blackOrWhiteColor(isLightTheme),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val reminderWeekDays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

private fun reminderMonthName(month: Month): String = when (month) {
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
