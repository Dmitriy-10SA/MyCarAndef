package com.andef.mycarandef.design.topbar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun colors(isLightTheme: Boolean) = TopAppBarDefaults.topAppBarColors(
    containerColor = if (isLightTheme) White else DarkGray,
    scrolledContainerColor = if (isLightTheme) White else DarkGray,
    navigationIconContentColor = if (isLightTheme) Black else White,
    titleContentColor = if (isLightTheme) Black else White,
    actionIconContentColor = if (isLightTheme) Black else White,
)