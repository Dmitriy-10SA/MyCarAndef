package com.andef.mycarandef.graph

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.andef.mycar.backup.presentation.backupmain.BackupMainScreen
import com.andef.mycar.backup.presentation.backupstart.BackupStartScreen
import com.andef.mycar.reminder.presentation.allreminders.AllRemindersScreen
import com.andef.mycar.reminder.presentation.reminderadd.ReminderAddScreen
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.presentation.caradd.CarAddScreen
import com.andef.mycarandef.expense.presentation.expenseadd.ExpenseAddScreen
import com.andef.mycarandef.expense.presentation.expenseanalysis.ExpenseAnalysisScreen
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.andef.mycarandef.work.presentation.workadd.WorkAddScreen
import java.time.LocalDate

@Composable
fun MyCarNavGraph(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isFirstStart: Boolean,
    isLightTheme: Boolean,
    mainContentIsVisible: Boolean,
    allCars: List<Car>,
    currentCarName: State<String>,
    currentCarImageUri: State<String?>,
    currentCarId: Long,
    startDate: LocalDate,
    endDate: LocalDate,
    onMainLeftSwipe: () -> Unit,
    onMainRightSwipe: () -> Unit
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
            currentCarId = currentCarId,
            startDate = startDate,
            endDate = endDate,
            onLeftSwipe = onMainLeftSwipe,
            onRightSwipe = onMainRightSwipe
        )
        composable(
            route = Screen.WorkScreen.route,
            arguments = listOf(
                navArgument(Screen.ID_PARAM) { type = NavType.LongType },
                navArgument(Screen.CAR_ID_PARAM) { type = NavType.LongType }
            ),
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
            val carId =
                it.arguments?.getLong(Screen.CAR_ID_PARAM) ?: throw IllegalArgumentException()
            WorkAddScreen(
                workId = id,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = carId
            )
        }
        composable(
            route = Screen.WorkAddScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
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
            arguments = listOf(
                navArgument(Screen.ID_PARAM) { type = NavType.LongType },
                navArgument(Screen.CAR_ID_PARAM) { type = NavType.LongType }
            ),
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
            val carId =
                it.arguments?.getLong(Screen.CAR_ID_PARAM) ?: throw IllegalArgumentException()
            ExpenseAddScreen(
                expenseId = id,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = carId
            )
        }
        composable(
            route = Screen.ExpenseAddScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            ExpenseAddScreen(
                expenseId = null,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId
            )
        }
        composable(
            route = Screen.ExpenseAnalysisScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            ExpenseAnalysisScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId,
                currentCarName = currentCarName,
                currentCarImageUri = currentCarImageUri,
                allCars = allCars
            )
        }
        composable(
            route = Screen.CarScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType }),
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
            CarAddScreen(
                carId = id,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                currentCarId = currentCarId
            )
        }
        composable(
            route = Screen.CarAddScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            CarAddScreen(
                carId = null,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                currentCarId = currentCarId
            )
        }
        composable(
            route = Screen.AllRemindersScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            AllRemindersScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId,
                currentCarName = currentCarName,
                currentCarImageUri = currentCarImageUri,
                allCars = allCars
            )
        }
        composable(
            route = Screen.ReminderAddScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            ReminderAddScreen(
                reminderId = null,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId,
                carName = currentCarName.value
            )
        }
        composable(
            route = Screen.ReminderScreen.route,
            arguments = listOf(navArgument(Screen.ID_PARAM) { type = NavType.LongType }),
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }
        ) {
            val id = it.arguments?.getLong(Screen.ID_PARAM) ?: throw IllegalArgumentException()
            ReminderAddScreen(
                reminderId = id,
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme,
                carId = currentCarId,
                carName = currentCarName.value
            )
        }
        composable(
            route = Screen.BackupMainScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            BackupMainScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme
            )
        }
        composable(
            route = Screen.BackupStartScreen.route,
            enterTransition = { fadeIn(tween(400, easing = FastOutSlowInEasing)) },
            exitTransition = { fadeOut(tween(400, easing = FastOutSlowInEasing)) }) {
            BackupStartScreen(
                navHostController = navHostController,
                viewModelFactory = viewModelFactory,
                paddingValues = paddingValues,
                isLightTheme = isLightTheme
            )
        }
    }
}