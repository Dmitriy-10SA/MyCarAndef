package com.andef.mycarandef.design.fab.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.White

@Composable
fun UiFAB(
    onClick: () -> Unit,
    icon: Painter,
    iconContentDescription: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(tween(700)),
        exit = scaleOut(tween(700))
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            containerColor = Blue,
            contentColor = White
        ) {
            Icon(
                painter = icon,
                contentDescription = iconContentDescription
            )
        }
    }
}

private val shape = CircleShape