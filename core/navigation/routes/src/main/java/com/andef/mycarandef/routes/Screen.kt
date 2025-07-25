package com.andef.mycarandef.routes

sealed class Screen(val route: String) {
    data object StartScreens : Screen(START_SCREENS) {
        data object UsernameInputScreen : Screen(USERNAME_INPUT_SCREEN)
        data object CarInputScreen : Screen(CAR_INPUT_SCREEN)

        private const val USERNAME_INPUT_SCREEN = "username-input-screen"
        private const val CAR_INPUT_SCREEN = "car-input-screen"
    }

    data object MainScreens : Screen(MAIN_SCREENS) {
        data object WorksMainScreen : Screen(WORKS_MAIN_SCREEN)
        data object ExpensesMainScreen : Screen(EXPENSES_MAIN_SCREEN)
        data object MapsMainScreen : Screen(MAPS_MAIN_SCREEN)
        data object CarsMainScreen : Screen(CARS_MAIN_SCREEN)

        private const val WORKS_MAIN_SCREEN = "works-main-screen"
        private const val EXPENSES_MAIN_SCREEN = "expenses-main-screen"
        private const val MAPS_MAIN_SCREEN = "maps-main-screen"
        private const val CARS_MAIN_SCREEN = "cars-main-screen"

        val allRoutes = listOf(
            WORKS_MAIN_SCREEN,
            EXPENSES_MAIN_SCREEN,
            MAPS_MAIN_SCREEN,
            CARS_MAIN_SCREEN,
        )
    }

    data object WorkAddScreen : Screen(WORK_ADD_SCREEN)

    data object ExpenseAddScreen : Screen(EXPENSE_ADD_SCREEN)

    data object CarAddScreen : Screen(CAR_ADD_SCREEN)

    data object WorkScreen : Screen("$WORK_SCREEN/{$ID_PARAM}/{$CAR_ID_PARAM}") {
        fun passId(id: Long, carId: Long): String = "$WORK_SCREEN/$id/$carId"
    }

    data object ExpenseScreen : Screen("$EXPENSE_SCREEN/{$ID_PARAM}/{$CAR_ID_PARAM}") {
        fun passId(id: Long, carId: Long): String = "$EXPENSE_SCREEN/$id/$carId"
    }

    data object ExpenseAnalysisScreen : Screen(EXPENSE_ANALYSIS_SCREEN)

    data object CarScreen : Screen("$CAR_SCREEN/{$ID_PARAM}") {
        fun passId(id: Long): String = "$CAR_SCREEN/$id"
    }

    data object AllRemindersScreen : Screen(ALL_REMINDERS_SCREEN)

    data object ReminderScreen : Screen("$REMINDER_SCREEN/{$ID_PARAM}") {
        fun passId(id: Long): String = "$REMINDER_SCREEN/$id"
    }

    data object ReminderAddScreen : Screen(REMINDER_ADD_SCREEN)

    data object BackupMainScreen : Screen(BACKUP_MAIN_SCREEN)

    data object BackupStartScreen : Screen(BACKUP_START_SCREEN)

    companion object {
        private const val START_SCREENS = "start-screens"
        private const val MAIN_SCREENS = "main-screens"
        private const val WORK_SCREEN = "work-screen"
        private const val WORK_ADD_SCREEN = "work-add-screen"
        private const val EXPENSE_SCREEN = "expense-screen"
        private const val EXPENSE_ADD_SCREEN = "expense-add-screen"
        private const val EXPENSE_ANALYSIS_SCREEN = "expense-analysis-screen"
        private const val CAR_SCREEN = "car-screen"
        private const val CAR_ADD_SCREEN = "car-add-screen"
        private const val ALL_REMINDERS_SCREEN = "all-reminders-screen"
        private const val REMINDER_SCREEN = "reminder-screen"
        private const val REMINDER_ADD_SCREEN = "reminder-add-screen"
        private const val BACKUP_MAIN_SCREEN = "backup-main-screen"
        private const val BACKUP_START_SCREEN = "backup-start-screen"

        const val ID_PARAM = "id-param"
        const val CAR_ID_PARAM = "car-id-param"
    }
}