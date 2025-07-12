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

@Composable
fun MyCarNavGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isFirstStart: Boolean,
    isLightTheme: Boolean
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
            isLightTheme = isLightTheme
        )
        composable(
            route = Screen.WorkScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType })
        ) {
            val id = it.arguments?.getInt(Screen.ID_PARAM)
        }
        composable(
            route = Screen.ExpenseScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType })
        ) {
            val id = it.arguments?.getInt(Screen.ID_PARAM)
        }
        composable(
            route = Screen.CarScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt(Screen.ID_PARAM)
        }
    }
}