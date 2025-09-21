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
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.BlackColor
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
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
    navigationIconTint: Color = Color.Unspecified,
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
                windowInsets = windowInsets,
                navigationIconTint = navigationIconTint
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
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
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    navigationIconTint: Color
) {
    val navIconTint = if (navigationIconTint != Color.Unspecified) {
        navigationIconTint
    } else {
        LocalContentColor.current
    }
    when (type) {
        UiTopBarType.Center -> {
            CenterAlignedTopAppBar(
                modifier = modifier,
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        fontSize = 22.sp,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    navigationIcon?.let {
                        IconButton(onClick = onNavigationIconClick) {
                            Icon(
                                tint = navIconTint,
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
                            tint = navIconTint,
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
                                    tint = navIconTint,
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
                    modifier = Modifier
                        .padding(horizontal = 1.dp, vertical = 3.dp)
                        .padding(bottom = 6.dp),
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
                                    tint = navIconTint,
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
                    containerColor = darkGrayOrWhiteColor(isLightTheme),
                    contentColor = blackOrWhiteColor(isLightTheme),
                    divider = {},
                    indicator = {
                        Box(
                            modifier = Modifier
                                .tabIndicatorOffset(type.selectedTabIndex)
                                .height(5.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 28.dp)
                                .background(
                                    color = GreenColor,
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
                                selectedContentColor = blackOrWhiteColor(isLightTheme),
                                unselectedContentColor = blackOrWhiteColor(isLightTheme),
                                text = {
                                    Text(
                                        text = tab.title,
                                        fontSize = 16.sp,
                                        color = if (tab.id == type.selectedTabIndex) {
                                            GreenColor
                                        } else {
                                            when (isLightTheme) {
                                                true -> BlackColor
                                                false -> WhiteColor
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
        GreenColor
    } else {
        Color.Transparent
    }
    val borderColor = when (day.date == LocalDate.now()) {
        true -> if (day.date != currentDate) {
            blackOrWhiteColor(isLightTheme)
        } else {
            Color.Transparent
        }

        false -> if (day.date != currentDate) {
            grayColor(isLightTheme).copy(alpha = 0.3f)
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
            AutoResizeText(
                modifier = Modifier.fillMaxWidth(),
                text = getShortDayOfWeekName(day.date.dayOfWeek),
                textAlign = TextAlign.Center,
                color = if (currentDate == day.date) {
                    WhiteColor
                } else {
                    blackOrWhiteColor(isLightTheme)
                }
            )
            AutoResizeText(
                modifier = Modifier.fillMaxWidth(),
                text = day.date.dayOfMonth.toString(),
                textAlign = TextAlign.Center,
                color = if (currentDate == day.date) {
                    WhiteColor
                } else {
                    blackOrWhiteColor(isLightTheme)
                }
            )
            if (withEvent(day.date)) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (day.date == currentDate) {
                                WhiteColor
                            } else {
                                GreenColor
                            },
                            shape = CircleShape
                        )
                )
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AutoResizeText(
    text: String,
    color: Color,
    textAlign: TextAlign = TextAlign.Center,
    maxFontSize: TextUnit = 16.sp,
    minFontSize: TextUnit = 2.sp,
    modifier: Modifier = Modifier
) {
    var textSize by remember { mutableStateOf(maxFontSize) }

    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        fontSize = textSize,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { result ->
            if (result.hasVisualOverflow && textSize > minFontSize) {
                textSize = (textSize.value - 1).sp
            }
        }
    )
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
    containerColor = darkGrayOrWhiteColor(isLightTheme = isLightTheme, duration = 260),
    scrolledContainerColor = darkGrayOrWhiteColor(isLightTheme = isLightTheme, duration = 260),
    navigationIconContentColor = blackOrWhiteColor(isLightTheme = isLightTheme, duration = 260),
    titleContentColor = blackOrWhiteColor(isLightTheme = isLightTheme, duration = 260),
    actionIconContentColor = blackOrWhiteColor(isLightTheme = isLightTheme, duration = 260),
)