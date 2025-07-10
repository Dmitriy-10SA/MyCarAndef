package com.andef.mycarandef.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory

fun NavGraphBuilder.mainScreensGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues
) {
    navigation(
        route = Screen.MainScreens.route,
        startDestination = Screen.MainScreens.WorksMainScreen.route
    ) {
        composable(route = Screen.MainScreens.WorksMainScreen.route) {

        }
        composable(route = Screen.MainScreens.ExpensesMainScreen.route) {

        }
        composable(route = Screen.MainScreens.MapsMainScreen.route) {

        }
        composable(route = Screen.MainScreens.CarsMainScreen.route) {

        }
    }
}