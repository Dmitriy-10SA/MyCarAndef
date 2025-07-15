package com.andef.mycarandef.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.andef.mycarandef.work.presentation.workadd.WorkAddScreen

@Composable
fun MyCarNavGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isFirstStart: Boolean,
    isLightTheme: Boolean,
    mainContentIsVisible: Boolean,
    currentCarId: Long
) {
    NavHost(
        navController = navHostController,
        startDestination = when (isFirstStart) {
            true -> Screen.StartScreens.route
            false -> Screen.MainScreens.route
        }
    ) {
        startScreenGraph(
            navHostController = navHostController,
            viewModelFactory = viewModelFactory,
            paddingValues = paddingValues,
            isLightTheme = isLightTheme
        )
        mainScreensGraph(
            navHostController = navHostController,
            viewModelFactory = viewModelFactory,
            paddingValues = paddingValues,
            isLightTheme = isLightTheme,
            mainContentIsVisible = mainContentIsVisible,
            currentCarId = currentCarId
        )
        composable(
            route = Screen.WorkScreen.route,
            arguments = listOf(
                navArgument(Screen.ID_PARAM) { type = NavType.LongType },
                navArgument(Screen.CAR_ID_PARAM) { type = NavType.LongType }
            )
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
            val carId = it.arguments?.getLong(Screen.CAR_ID_PARAM) ?: throw IllegalArgumentException()

        }
        composable(route = Screen.WorkAddScreen.route) {
            WorkAddScreen(
                workId = null,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId
            )
        }
        composable(
            route = Screen.ExpenseScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType })
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
        }
        composable(route = Screen.ExpenseAddScreen.route) {

        }
        composable(
            route = Screen.CarScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType })
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
        }
        composable(route = Screen.CarAddScreen.route) {

        }
    }
}