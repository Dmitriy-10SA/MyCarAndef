package com.andef.mycarandef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.card.date.ui.UiDateCard
import com.andef.mycarandef.design.card.expense.ui.UiExpenseCard
import com.andef.mycarandef.design.fab.ui.UiFAB
import com.andef.mycarandef.design.navigationbar.item.UiNavigationBarItem
import com.andef.mycarandef.design.navigationbar.ui.UiBottomBar
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyCarApp).component }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val isLightTheme = component.getIsLightThemeUseCase(isSystemInDarkTheme())
            val systemUiController = rememberSystemUiController()
            val navHostController = rememberNavController()
            SystemUiSettings(systemUiController = systemUiController, isLightTheme = isLightTheme)
            MainContent(
                navHostController = navHostController,
                component = component,
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun SystemUiSettings(systemUiController: SystemUiController, isLightTheme: Boolean) {
    with(systemUiController) {
        val color = when (isLightTheme) {
            true -> White
            false -> DarkGray
        }
        setNavigationBarColor(color = color, darkIcons = isLightTheme)
        setStatusBarColor(color = color, darkIcons = isLightTheme)
    }
}

@Composable
private fun MainContent(
    navHostController: NavHostController,
    component: MyCarComponent,
    isLightTheme: Boolean
) {
    MyCarAndefTheme(darkTheme = !isLightTheme) {
//        MyCarNavGraph(
//            navHostController = navHostController,
//            viewModelFactory = component.viewModelFactory,
//            paddingValues = PaddingValues(0.dp),
//            isFirstStart = component.getIsFirstStartUseCase()
//        )
        var value1 by rememberSaveable { mutableStateOf("") }
        var value2 by rememberSaveable { mutableStateOf("") }
        var route by rememberSaveable { mutableStateOf("1") }
        var navVisible by rememberSaveable { mutableStateOf(true) }
        UiScaffold(
            isLightTheme = isLightTheme,
            topBar = {
                UiTopBar(
                    isLightTheme = isLightTheme,
                    type = UiTopBarType.NotCenter,
                    title = "Привет, Дмитрий!",
                    navigationIcon = painterResource(com.andef.mycarandef.design.R.drawable.menu),
                    actions = {
                        IconButton(onClick = { navVisible = !navVisible }) {
                            Icon(
                                painter = painterResource(com.andef.mycarandef.design.R.drawable.attach),
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            bottomBar = {
                UiBottomBar(
                    isLightTheme = isLightTheme,
                    itemSelected = { item -> item.route == route },
                    onItemClick = { item -> route = item.route },
                    items = listOf(
                        UiNavigationBarItem(
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.works),
                            contentDescription = "",
                            route = "1",
                            title = "Работы"
                        ),
                        UiNavigationBarItem(
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.ruble),
                            contentDescription = "",
                            route = "2",
                            title = "Траты"
                        ),
                        UiNavigationBarItem(
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.location),
                            contentDescription = "",
                            route = "3",
                            title = "Карта"
                        ),
                        UiNavigationBarItem(
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.car),
                            contentDescription = "",
                            route = "4",
                            title = "Гараж"
                        )
                    )
                )
            },
            floatingActionButton = {
                UiFAB(
                    onClick = {},
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.add),
                    isVisible = route == "2" || route == "1" || route == "4",
                    iconContentDescription = ""
                )
            }
        ) {
            val items = listOf<Expense>(
                Expense(
                    id = 1L,
                    note = null,
                    amount = 21353.0021,
                    type = ExpenseType.FUEL,
                    date = LocalDate.of(2025, 1, 21),
                    carId = 0
                ),
                Expense(
                    id = 2L,
                    note = null,
                    amount = 121.31,
                    type = ExpenseType.WORKS,
                    date = LocalDate.of(2025, 1, 21),
                    carId = 0
                ),
                Expense(
                    id = 3L,
                    note = null,
                    amount = 10004.0021,
                    type = ExpenseType.WASHING,
                    date = LocalDate.of(2025, 1, 16),
                    carId = 0
                ),
                Expense(
                    id = 4L,
                    note = "Очень-очень-очень-очень длинное примечание",
                    amount = 1030109414.1231,
                    type = ExpenseType.OTHER,
                    date = LocalDate.of(2025, 1, 13),
                    carId = 0
                )
            )
            var lastDate by rememberSaveable { mutableStateOf(LocalDate.MIN) }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = items, key = { it.id }) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (it.date != lastDate || items[0] == it) {
                            lastDate = it.date
                            UiDateCard(
                                isLightTheme = isLightTheme,
                                date = it.date
                            )
                        }
                        UiExpenseCard(
                            isLightTheme = isLightTheme,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            expense = it,
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}