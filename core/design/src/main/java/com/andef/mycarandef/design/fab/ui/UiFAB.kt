package com.andef.mycarandef.design.fab.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.WhiteColor

@Composable
fun UiFAB(
    onClick: () -> Unit,
    icon: Painter,
    iconContentDescription: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
) {
    if (isVisible) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            containerColor = GreenColor,
            contentColor = WhiteColor
        ) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription
            )
        }
    }
}

private val shape = CircleShape