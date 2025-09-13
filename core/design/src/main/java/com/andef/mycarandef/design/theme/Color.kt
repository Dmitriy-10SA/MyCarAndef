package com.andef.mycarandef.design.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val GreenColor = Color(0xFF2ECC71)

val RedColor = Color(0xFFFF4848)

val WhiteColor = Color(0xFFFFFFFF)

val BlackColor = Color(0xFF000000)

val DarkGrayColor = Color(0xFF121212)
val GrayForLightColor = Color(0xFF8B8F92)
val GrayForDarkColor = Color(0xFFB6BABD)
val YellowColor =  Color(0xFFFFB800)

private fun anim(duration: Int): TweenSpec<Color> =
    tween(durationMillis = duration, easing = FastOutSlowInEasing)

@Composable
fun grayColor(isLightTheme: Boolean, duration: Int = 800) = animateColorAsState(
    targetValue = if (isLightTheme) GrayForLightColor else GrayForDarkColor,
    animationSpec = anim(duration)
).value

@Composable
fun blackOrWhiteColor(isLightTheme: Boolean, duration: Int = 800) = animateColorAsState(
    targetValue = if (isLightTheme) BlackColor else WhiteColor,
    animationSpec = anim(duration)
).value

@Composable
fun darkGrayOrWhiteColor(isLightTheme: Boolean, duration: Int = 800) = animateColorAsState(
    targetValue = if (isLightTheme) WhiteColor else DarkGrayColor,
    animationSpec = anim(duration)
).value