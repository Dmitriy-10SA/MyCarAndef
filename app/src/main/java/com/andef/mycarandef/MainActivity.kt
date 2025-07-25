package com.andef.mycarandef

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.car.ui.UiCarInBottomSheetCard
import com.andef.mycarandef.design.fab.ui.UiFAB
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.graph.MyCarNavGraph
import com.andef.mycarandef.routes.Screen
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyCarApp).component }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val isLightTheme = component.getIsLightThemeUseCase(isSystemInDarkTheme())
            val isLightThemeAsFlow = component.getIsLightThemeAsFlowUseCase
                .invoke()
                .collectAsState(isLightTheme)
            val systemUiController = rememberSystemUiController()
            val navHostController = rememberNavController()
            val navBackStackEntry = navHostController.currentBackStackEntryAsState().value
            val context = LocalContext.current
            val currentCarId = component.getCurrentCarIdAsFlowUseCase
                .invoke()
                .collectAsState(component.getCurrentCarIdUseCase.invoke())
            val currentCarName = component.getCurrentCarNameAsFlowUseCase
                .invoke()
                .collectAsState(component.getCurrentCarNameUseCase.invoke())
            val currentCarImageUri = component.getCurrentCarImageUriAsFlowUseCase
                .invoke()
                .collectAsState(component.getCurrentCarImageUriUseCase.invoke())
            val sheetState = rememberModalBottomSheetState()
            val sheetVisible = rememberSaveable { mutableStateOf(false) }
            val allCars = component.getAllCarsUseCase.invoke().collectAsState(listOf())
            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val username = component.getUsernameAsFlowUseCase
                .invoke()
                .collectAsState(component.getUsernameUseCase.invoke())

            SystemUiSettings(
                systemUiController = systemUiController,
                isLightTheme = isLightThemeAsFlow.value
            )
            MainContent(
                navBackStackEntry = navBackStackEntry,
                navHostController = navHostController,
                component = component,
                isLightTheme = isLightThemeAsFlow.value,
                context = context,
                currentCarName = currentCarName,
                currentCarId = currentCarId,
                currentCarImageUri = currentCarImageUri,
                sheetState = sheetState,
                sheetVisible = sheetVisible,
                allCars = allCars.value,
                drawerState = drawerState,
                scope = scope,
                username = username.value
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    drawerState: DrawerState,
    scope: CoroutineScope,
    navBackStackEntry: NavBackStackEntry?,
    navHostController: NavHostController,
    component: MyCarComponent,
    isLightTheme: Boolean,
    context: Context,
    sheetState: SheetState,
    allCars: List<Car>,
    username: String?,
    sheetVisible: MutableState<Boolean>,
    currentCarId: androidx.compose.runtime.State<Long>,
    currentCarImageUri: androidx.compose.runtime.State<String?>,
    currentCarName: androidx.compose.runtime.State<String>
) {
    MyCarAndefTheme(darkTheme = !isLightTheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes,
            drawerContent = {
                MainModalDrawerSheetContent(
                    username = username,
                    scope = scope,
                    drawerState = drawerState,
                    component = component,
                    isLightTheme = isLightTheme,
                    navHostController = navHostController
                )
            },
            content = {
                UiScaffold(
                    isLightTheme = isLightTheme,
                    bottomBar = {
                        MainBottomBar(
                            isLightTheme = isLightTheme,
                            navBackStackEntry = navBackStackEntry,
                            navHostController = navHostController
                        )
                    },
                    topBar = {
                        MainTopBar(
                            isLightTheme = isLightTheme,
                            navBackStackEntry = navBackStackEntry,
                            currentCarImageUri = currentCarImageUri,
                            context = context,
                            scope = scope,
                            drawerState = drawerState,
                            sheetVisible = sheetVisible,
                            currentCarName = currentCarName
                        )
                    },
                    floatingActionButton = {
                        MainFAB(
                            navBackStackEntry = navBackStackEntry,
                            navHostController = navHostController
                        )
                    }
                ) { paddingValues ->
                    MyCarNavGraph(
                        navHostController = navHostController,
                        viewModelFactory = component.viewModelFactory,
                        paddingValues = paddingValues,
                        isFirstStart = component.getIsFirstStartUseCase(),
                        allCars = allCars,
                        isLightTheme = isLightTheme,
                        mainContentIsVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes,
                        currentCarId = currentCarId.value,
                        currentCarName = currentCarName,
                        currentCarImageUri = currentCarImageUri
                    )
                    MainBottomSheet(
                        isLightTheme = isLightTheme,
                        allCars = allCars,
                        sheetState = sheetState,
                        sheetVisible = sheetVisible,
                        currentCarId = currentCarId.value,
                        component = component,
                        context = context
                    )
                }
            }
        )
    }
}

@Composable
private fun MainFAB(navBackStackEntry: NavBackStackEntry?, navHostController: NavHostController) {
    val currentRoute = navBackStackEntry?.destination?.route
    val allRoutesForMainFAB = listOf(
        Screen.MainScreens.WorksMainScreen.route,
        Screen.MainScreens.ExpensesMainScreen.route,
        Screen.MainScreens.CarsMainScreen.route
    )
    UiFAB(
        isVisible = currentRoute in allRoutesForMainFAB,
        icon = painterResource(R.drawable.my_car_add),
        iconContentDescription = "Крестик добавить",
        onClick = {
            when (currentRoute) {
                Screen.MainScreens.WorksMainScreen.route -> {
                    navHostController.navigate(Screen.WorkAddScreen.route)
                }

                Screen.MainScreens.ExpensesMainScreen.route -> {
                    navHostController.navigate(Screen.ExpenseAddScreen.route)
                }

                Screen.MainScreens.CarsMainScreen.route -> {
                    navHostController.navigate(Screen.CarAddScreen.route)
                }
            }
        }
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainBottomSheet(
    isLightTheme: Boolean,
    allCars: List<Car>,
    sheetState: SheetState,
    component: MyCarComponent,
    sheetVisible: MutableState<Boolean>,
    currentCarId: Long,
    context: Context
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    UiModalBottomSheet(
        modifier = Modifier.padding(top = screenHeight / 3),
        onDismissRequest = { sheetVisible.value = false },
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        isVisible = sheetVisible.value
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center,
            text = "Выбор текущего автомобиля",
            fontSize = 16.sp,
            color = if (isLightTheme) GrayForLight else GrayForDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }
            items(items = allCars, key = { it.id }) { car ->
                UiCarInBottomSheetCard(
                    onClick = {
                        sheetVisible.value = false
                        component.setCurrentCarIdUseCase.invoke(car.id)
                        component.setCurrentCarNameUseCase.invoke("${car.brand} ${car.model}")
                        component.setCurrentCarImageUriUseCase.invoke(car.photo)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .animateItem(),
                    isLightTheme = isLightTheme,
                    isCurrentCar = currentCarId == car.id,
                    car = car,
                    context = context
                )
            }
            item { Spacer(modifier = Modifier.height(0.dp)) }
        }
    }
}