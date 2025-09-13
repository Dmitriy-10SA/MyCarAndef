package com.andef.mycarandef.graph

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.start.presentation.carinput.CarInputScreen
import com.andef.mycarandef.start.presentation.usernameinput.UsernameInputScreen
import com.andef.mycarandef.viewmodel.ViewModelFactory

fun NavGraphBuilder.startScreenGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    navigation(
        route = Screen.StartScreens.route,
        startDestination = Screen.StartScreens.UsernameInputScreen.route
    ) {
        composable(
            route = Screen.StartScreens.UsernameInputScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            UsernameInputScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme
            )
        }
        composable(
            route = Screen.StartScreens.CarInputScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            CarInputScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme
            )
        }
    }
}