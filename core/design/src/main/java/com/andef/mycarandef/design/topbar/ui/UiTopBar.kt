package com.andef.mycarandef.design.topbar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.core.WeekDay
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTopBar(
    isLightTheme: Boolean,
    type: UiTopBarType,
    title: String,
    navigationIcon: Painter? = null,
    navigationIconContentDescription: String? = null,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    isVisible: Boolean = true,
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets
) {
    if (isVisible) {
        Column {
            MainContent(
                modifier = Modifier.fillMaxWidth(),
                isLightTheme = isLightTheme,
                title = title,
                navigationIcon = navigationIcon,
                navigationIconContentDescription = navigationIconContentDescription,
                type = type,
                onNavigationIconClick = onNavigationIconClick,
                actions = actions,
                expandedHeight = expandedHeight,
                windowInsets = windowInsets
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    isLightTheme: Boolean,
    type: UiTopBarType,
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: Painter? = null,
    navigationIconContentDescription: String? = null,
    onNavigationIconClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets
) {
    when (type) {
        UiTopBarType.Center -> {
            CenterAlignedTopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 20.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    navigationIcon?.let {
                        IconButton(onClick = onNavigationIconClick) {
                            Icon(
                                painter = it,
                                contentDescription = navigationIconContentDescription
                            )
                        }
                    }
                },
                actions = actions,
                expandedHeight = expandedHeight,
                windowInsets = windowInsets,
                colors = colors(isLightTheme = isLightTheme)
            )
        }

        UiTopBarType.NotCenter -> TopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    maxLines = 1,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                navigationIcon?.let {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(
                            painter = it,
                            contentDescription = navigationIconContentDescription
                        )
                    }
                }
            },
            actions = actions,
            expandedHeight = expandedHeight,
            windowInsets = windowInsets,
            colors = colors(isLightTheme = isLightTheme)
        )

        is UiTopBarType.WithCalendar -> {
            Column {
                TopAppBar(
                    modifier = modifier,
                    title = {
                        Text(
                            text = title,
                            maxLines = 1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        navigationIcon?.let {
                            IconButton(onClick = onNavigationIconClick) {
                                Icon(
                                    painter = it,
                                    contentDescription = navigationIconContentDescription
                                )
                            }
                        }
                    },
                    actions = actions,
                    expandedHeight = expandedHeight,
                    windowInsets = windowInsets,
                    colors = colors(isLightTheme = isLightTheme)
                )
                WeekCalendar(
                    modifier = Modifier.padding(horizontal = 1.dp, vertical = 3.dp),
                    state = type.weekCalendarState,
                    dayContent = { day ->
                        Day(
                            day = day,
                            isLightTheme = isLightTheme,
                            currentDate = type.currentDay,
                            onDayClick = type.onDayClick,
                            withEvent = type.withEvent
                        )
                    }
                )
            }
        }

        is UiTopBarType.WithTabs -> {
            Column {
                TopAppBar(
                    modifier = modifier,
                    title = {
                        Text(
                            text = title,
                            maxLines = 1,
                            fontSize = 20.sp,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        navigationIcon?.let {
                            IconButton(onClick = onNavigationIconClick) {
                                Icon(
                                    painter = it,
                                    contentDescription = navigationIconContentDescription
                                )
                            }
                        }
                    },
                    actions = actions,
                    expandedHeight = expandedHeight,
                    windowInsets = windowInsets,
                    colors = colors(isLightTheme = isLightTheme)
                )
                PrimaryScrollableTabRow(
                    edgePadding = 0.dp,
                    selectedTabIndex = type.selectedTabIndex,
                    modifier = modifier,
                    containerColor = if (isLightTheme) White else DarkGray,
                    contentColor = if (isLightTheme) Black else White,
                    indicator = {
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(type.selectedTabIndex)
                                .height(5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 28.dp)
                                .background(
                                    color = Blue,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        )
                    },
                    tabs = {
                        type.tabs.forEach { tab ->
                            Tab(
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                selected = tab.id == type.selectedTabIndex,
                                onClick = { type.onTabClick(tab) },
                                selectedContentColor = if (isLightTheme) Black else White,
                                unselectedContentColor = if (isLightTheme) Black else White,
                                text = {
                                    Text(
                                        text = tab.title,
                                        fontSize = 16.sp,
                                        color = if (tab.id == type.selectedTabIndex) {
                                            Blue
                                        } else {
                                            when (isLightTheme) {
                                                true -> Black
                                                false -> White
                                            }
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun Day(
    isLightTheme: Boolean,
    day: WeekDay,
    currentDate: LocalDate,
    onDayClick: (LocalDate) -> Unit,
    withEvent: (LocalDate) -> Boolean
) {
    val backgroundColor = if (currentDate == day.date) {
        Blue
    } else {
        Color.Transparent
    }
    val borderColor = when (day.date == LocalDate.now()) {
        true -> if (day.date != currentDate) {
            if (isLightTheme) Black else White
        } else {
            Color.Transparent
        }

        false -> if (day.date != currentDate) {
            if (isLightTheme) GrayForLight.copy(alpha = 0.3f) else GrayForDark.copy(alpha = 0.3f)
        } else {
            Color.Transparent
        }
    }

    Box(
        modifier = Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { onDayClick(day.date) })
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = getShortDayOfWeekName(day.date.dayOfWeek),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = if (currentDate == day.date) {
                    White
                } else {
                    if (isLightTheme) Black else White
                }
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = day.date.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = if (currentDate == day.date) {
                    White
                } else {
                    if (isLightTheme) Black else White
                }
            )
            Spacer(modifier = Modifier.height(1.dp))
            if (withEvent(day.date)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (day.date == currentDate) {
                                White
                            } else {
                                Blue
                            },
                            shape = CircleShape
                        )
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
    }
}

@Composable
private fun getShortDayOfWeekName(dayOfWeek: DayOfWeek) = when (dayOfWeek) {
    DayOfWeek.MONDAY -> "Пн"
    DayOfWeek.TUESDAY -> "Вт"
    DayOfWeek.WEDNESDAY -> "Ср"
    DayOfWeek.THURSDAY -> "Чт"
    DayOfWeek.FRIDAY -> "Пт"
    DayOfWeek.SATURDAY -> "Сб"
    DayOfWeek.SUNDAY -> "Вс"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun colors(isLightTheme: Boolean) = TopAppBarDefaults.topAppBarColors(
    containerColor = if (isLightTheme) White else DarkGray,
    scrolledContainerColor = if (isLightTheme) White else DarkGray,
    navigationIconContentColor = if (isLightTheme) Black else White,
    titleContentColor = if (isLightTheme) Black else White,
    actionIconContentColor = if (isLightTheme) Black else White,
)