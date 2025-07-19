package com.andef.mycarandef

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.navigationbar.item.UiNavigationBarItem
import com.andef.mycarandef.design.navigationbar.ui.UiNavigationBar
import com.andef.mycarandef.routes.Screen

@Composable
fun MainBottomBar(
    isLightTheme: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    navHostController: NavHostController
) {
    UiNavigationBar(
        isLightTheme = isLightTheme,
        itemSelected = { item -> item.route == navBackStackEntry?.destination?.route },
        onItemClick = { item ->
            onMainScreenItemClick(
                item = item,
                navHostController = navHostController,
                navBackStackEntry = navBackStackEntry
            )
        },
        items = mainScreenItems(),
        isVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes
    )
}

private fun onMainScreenItemClick(
    item: UiNavigationBarItem,
    navHostController: NavHostController,
    navBackStackEntry: NavBackStackEntry?
) {
    if (item.route != navBackStackEntry?.destination?.route) {
        navHostController.navigate(item.route) {
            popUpTo(Screen.MainScreens.WorksMainScreen.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

@Composable
private fun mainScreenItems() = listOf(
    UiNavigationBarItem(
        icon = painterResource(R.drawable.my_car_works),
        contentDescription = "Иконка работы",
        title = "Работы",
        route = Screen.MainScreens.WorksMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.my_car_ruble),
        contentDescription = "Иконка рубля",
        title = "Траты",
        Screen.MainScreens.ExpensesMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.my_car_location),
        contentDescription = "Иконка локация значок",
        title = "Карта",
        Screen.MainScreens.MapsMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.my_car_car),
        contentDescription = "Иконка машины",
        title = "Гараж",
        Screen.MainScreens.CarsMainScreen.route
    )
)