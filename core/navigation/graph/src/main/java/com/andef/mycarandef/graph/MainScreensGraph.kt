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
import com.andef.mycarandef.car.presentation.carmain.CarMainScreen
import com.andef.mycarandef.expense.presentation.expensemain.ExpenseMainScreen
import com.andef.mycarandef.map.presentation.MapMainScreen
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.andef.mycarandef.work.presentation.workmain.WorkMainScreen
import java.time.LocalDate

fun NavGraphBuilder.mainScreensGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    mainContentIsVisible: Boolean,
    currentCarId: Long,
    startDate: LocalDate,
    endDate: LocalDate
) {
    navigation(
        route = Screen.MainScreens.route,
        startDestination = Screen.MainScreens.WorksMainScreen.route
    ) {
        composable(
            route = Screen.MainScreens.WorksMainScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }
        ) {
            if (mainContentIsVisible) {
                WorkMainScreen(
                    navHostController = navHostController,
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId
                )
            }
        }
        composable(
            route = Screen.MainScreens.ExpensesMainScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            if (mainContentIsVisible) {
                ExpenseMainScreen(
                    navHostController = navHostController,
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId,
                    startDate = startDate,
                    endDate = endDate
                )
            }
        }
        composable(
            route = Screen.MainScreens.MapsMainScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            if (mainContentIsVisible) {
                MapMainScreen(
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId
                )
            }
        }
        composable(
            route = Screen.MainScreens.CarsMainScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            if (mainContentIsVisible) {
                CarMainScreen(
                    navHostController = navHostController,
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId
                )
            }
        }
    }
}