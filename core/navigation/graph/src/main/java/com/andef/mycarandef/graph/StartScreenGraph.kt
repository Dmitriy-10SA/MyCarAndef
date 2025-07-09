package com.andef.mycarandef.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.andef.mycarandef.di.viewmodel.ViewModelFactory
import com.andef.mycarandef.routes.Screen

fun NavGraphBuilder.startScreenGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues
) {
    navigation(
        route = Screen.StartScreens.route,
        startDestination = Screen.StartScreens.UsernameInputScreen.route
    ) {
        composable(route = Screen.StartScreens.UsernameInputScreen.route) {

        }
        composable(route = Screen.StartScreens.CarInputScreen.route) {

        }
    }
}