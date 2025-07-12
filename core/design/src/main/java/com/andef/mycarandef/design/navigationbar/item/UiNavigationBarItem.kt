package com.andef.mycarandef.design.navigationbar.item

import androidx.compose.ui.graphics.painter.Painter

open class UiNavigationBarItem(
    val icon: Painter,
    val contentDescription: String,
    val title: String,
    val route: String
)