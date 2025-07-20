package com.andef.mycarandef.graph

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

fun NavGraphBuilder.mainScreensGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    mainContentIsVisible: Boolean,
    currentCarId: Long
) {
    navigation(
        route = Screen.MainScreens.route,
        startDestination = Screen.MainScreens.WorksMainScreen.route
    ) {
        composable(route = Screen.MainScreens.WorksMainScreen.route) {
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
        composable(route = Screen.MainScreens.ExpensesMainScreen.route) {
            if (mainContentIsVisible) {
                ExpenseMainScreen(
                    navHostController = navHostController,
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId
                )
            }
        }
        composable(route = Screen.MainScreens.MapsMainScreen.route) {
            if (mainContentIsVisible) {
                MapMainScreen(
                    viewModelFactory = viewModelFactory,
                    paddingValues = paddingValues,
                    isLightTheme = isLightTheme,
                    currentCarId = currentCarId
                )
            }
        }
        composable(route = Screen.MainScreens.CarsMainScreen.route) {
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