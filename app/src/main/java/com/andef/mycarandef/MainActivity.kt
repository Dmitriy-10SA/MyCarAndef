package com.andef.mycarandef

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.car.ui.UiCarInBottomSheetCard
import com.andef.mycarandef.design.fab.ui.UiFAB
import com.andef.mycarandef.design.navigationbar.item.UiNavigationBarItem
import com.andef.mycarandef.design.navigationbar.ui.UiNavigationBar
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.graph.MyCarNavGraph
import com.andef.mycarandef.routes.Screen
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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

            SystemUiSettings(systemUiController = systemUiController, isLightTheme = isLightTheme)
            MainContent(
                navBackStackEntry = navBackStackEntry,
                navHostController = navHostController,
                component = component,
                isLightTheme = isLightTheme,
                context = context,
                currentCarName = currentCarName,
                currentCarId = currentCarId,
                currentCarImageUri = currentCarImageUri,
                sheetState = sheetState,
                sheetVisible = sheetVisible,
                allCars = allCars.value
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
    navBackStackEntry: NavBackStackEntry?,
    navHostController: NavHostController,
    component: MyCarComponent,
    isLightTheme: Boolean,
    context: Context,
    sheetState: SheetState,
    allCars: List<Car>,
    sheetVisible: MutableState<Boolean>,
    currentCarId: androidx.compose.runtime.State<Long>,
    currentCarImageUri: androidx.compose.runtime.State<String?>,
    currentCarName: androidx.compose.runtime.State<String>
) {
    MyCarAndefTheme(darkTheme = !isLightTheme) {
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
                isLightTheme = isLightTheme,
                mainContentIsVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes,
                currentCarId = currentCarId.value
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
        icon = painterResource(R.drawable.add),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainTopBar(
    isLightTheme: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    context: Context,
    sheetVisible: MutableState<Boolean>,
    currentCarName: androidx.compose.runtime.State<String>,
    currentCarImageUri: androidx.compose.runtime.State<String?>,
) {
    UiTopBar(
        isLightTheme = isLightTheme,
        type = UiTopBarType.NotCenter,
        title = currentCarName.value,
        navigationIcon = painterResource(R.drawable.menu),
        navigationIconContentDescription = "Меню",
        onNavigationIconClick = { TODO() },
        actions = {
            CarPhoto(
                currentCarImageUri = currentCarImageUri,
                isLightTheme = isLightTheme,
                context = context
            )
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (isLightTheme) Black else White
                ),
                onClick = { sheetVisible.value = true }
            ) {
                Icon(
                    tint = if (isLightTheme) Black else White,
                    painter = painterResource(R.drawable.keyboard_arrow_down),
                    contentDescription = "Выбор машины"
                )
            }
        },
        isVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes
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
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Выбор текущего автомобиля",
                    fontSize = 16.sp,
                    color = if (isLightTheme) GrayForLight else GrayForDark
                )
            }
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
                        .animateItem(),
                    isLightTheme = isLightTheme,
                    isCurrentCar = currentCarId == car.id,
                    car = car,
                    context = context
                )
            }
            item { Spacer(modifier = Modifier.height(0.dp)) }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun CarPhoto(
    currentCarImageUri: androidx.compose.runtime.State<String?>,
    isLightTheme: Boolean,
    context: Context
) {
    if (!currentCarImageUri.value.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(currentCarImageUri.value)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.car_wo_photo),
            error = painterResource(R.drawable.car_wo_photo),
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = if (isLightTheme) {
                        GrayForLight.copy(alpha = 0.3f)
                    } else {
                        GrayForDark.copy(alpha = 0.3f)
                    }
                ),
            contentScale = ContentScale.Crop,
            contentDescription = "Фото машины"
        )
    } else {
        Image(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = if (isLightTheme) {
                        GrayForLight.copy(alpha = 0.3f)
                    } else {
                        GrayForDark.copy(alpha = 0.3f)
                    }
                ),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.car_wo_photo),
            contentDescription = "Иконка машины"
        )
    }
}

@Composable
private fun MainBottomBar(
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
        icon = painterResource(R.drawable.works),
        contentDescription = "Иконка работы",
        title = "Работы",
        route = Screen.MainScreens.WorksMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.ruble),
        contentDescription = "Иконка рубля",
        title = "Траты",
        Screen.MainScreens.ExpensesMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.location),
        contentDescription = "Иконка локация значок",
        title = "Карта",
        Screen.MainScreens.MapsMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(R.drawable.car),
        contentDescription = "Иконка машины",
        title = "Гараж",
        Screen.MainScreens.CarsMainScreen.route
    )
)