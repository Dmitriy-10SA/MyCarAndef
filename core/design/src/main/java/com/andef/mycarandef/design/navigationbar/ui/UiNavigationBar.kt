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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.navigationbar.item.UiNavigationBarItem
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.White

@Composable
fun UiBottomBar(
    isLightTheme: Boolean,
    itemSelected: (UiNavigationBarItem) -> Boolean,
    onItemClick: (UiNavigationBarItem) -> Unit,
    items: List<UiNavigationBarItem>
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
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
        containerColor = if (isLightTheme) White else DarkGray,
        contentColor = if (isLightTheme) Black else White
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = itemSelected(item),
                onClick = { onItemClick(item) },
                icon = { Icon(painter = item.icon, contentDescription = item.contentDescription) },
                label = { Text(text = item.title, fontSize = 12.sp) },
                alwaysShowLabel = true,
                colors = colors(isLightTheme = isLightTheme)
            )
        }
    }
}

@Composable
private fun colors(isLightTheme: Boolean) = NavigationBarItemDefaults.colors(
    selectedTextColor = Blue,
    selectedIconColor = Blue,
    indicatorColor = Color.Transparent,
    unselectedTextColor = if (isLightTheme) Black else White,
    unselectedIconColor = if (isLightTheme) Black else White
)