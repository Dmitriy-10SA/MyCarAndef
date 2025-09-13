package com.andef.mycarandef.design.navigationbar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.navigationbar.item.UiNavigationBarItem
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor

@Composable
fun UiNavigationBar(
    isLightTheme: Boolean,
    itemSelected: (UiNavigationBarItem) -> Boolean,
    onItemClick: (UiNavigationBarItem) -> Unit,
    items: List<UiNavigationBarItem>,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Column {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
            )
            MainContent(
                modifier = Modifier.fillMaxWidth(),
                isLightTheme = isLightTheme,
                itemSelected = itemSelected,
                onItemClick = onItemClick,
                items = items
            )
        }
    }
}

@Composable
private fun MainContent(
    isLightTheme: Boolean,
    modifier: Modifier = Modifier,
    itemSelected: (UiNavigationBarItem) -> Boolean,
    onItemClick: (UiNavigationBarItem) -> Unit,
    items: List<UiNavigationBarItem>
) {
    NavigationBar(
        modifier = modifier,
        containerColor = darkGrayOrWhiteColor(isLightTheme),
        contentColor = blackOrWhiteColor(isLightTheme)
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = itemSelected(item),
                onClick = { onItemClick(item) },
                icon = { Icon(painter = item.icon, contentDescription = item.contentDescription) },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = colors(isLightTheme = isLightTheme)
            )
        }
    }
}

@Composable
private fun colors(isLightTheme: Boolean) = NavigationBarItemDefaults.colors(
    selectedTextColor = GreenColor,
    selectedIconColor = GreenColor,
    indicatorColor = Color.Transparent,
    unselectedTextColor = blackOrWhiteColor(isLightTheme),
    unselectedIconColor = blackOrWhiteColor(isLightTheme)
)