package com.andef.mycarandef.design.loading.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.White

@Composable
fun UiLoading(isVisible: Boolean, paddingValues: PaddingValues, isLightTheme: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(700)),
        exit = fadeOut(tween(700))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = shape,
                border = BorderStroke(width = 2.dp, color = if (isLightTheme) Black else White),
                colors = colors(isLightTheme = isLightTheme)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 48.dp, vertical = 18.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "MyCar", fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    CircularProgressIndicator(
                        color = Blue,
                        trackColor = if (isLightTheme) Black else White
                    )
                }
            }
        }
    }
}

private val shape = RoundedCornerShape(36.dp)

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = if (isLightTheme) White else DarkGray,
    contentColor = if (isLightTheme) Black else White
)