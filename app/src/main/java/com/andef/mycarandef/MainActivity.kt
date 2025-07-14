package com.andef.mycarandef

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.R
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
            SystemUiSettings(systemUiController = systemUiController, isLightTheme = isLightTheme)
            MainContent(
                navBackStackEntry = navBackStackEntry,
                navHostController = navHostController,
                component = component,
                isLightTheme = isLightTheme,
                context = context
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
    navBackStackEntry: NavBackStackEntry?,
    navHostController: NavHostController,
    component: MyCarComponent,
    isLightTheme: Boolean,
    context: Context
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
                    component = component,
                    context = context
                )
            }
        ) { paddingValues ->
            MyCarNavGraph(
                navHostController = navHostController,
                viewModelFactory = component.viewModelFactory,
                paddingValues = paddingValues,
                isFirstStart = component.getIsFirstStartUseCase(),
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun MainTopBar(
    isLightTheme: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    component: MyCarComponent,
    context: Context
) {
    UiTopBar(
        isLightTheme = isLightTheme,
        type = UiTopBarType.NotCenter,
        title = component.getCurrentCarNameUseCase.invoke(),
        navigationIcon = painterResource(com.andef.mycarandef.design.R.drawable.menu),
        navigationIconContentDescription = "Меню",
        onNavigationIconClick = { TODO() },
        actions = {
            CarPhoto(component = component, isLightTheme = isLightTheme, context = context)
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = if (isLightTheme) Black else White
                ),
                onClick = { TODO() }
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

@Composable
private fun CarPhoto(component: MyCarComponent, isLightTheme: Boolean, context: Context) {
    if (!component.getCurrentCarImageUri.invoke().isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(component.getCurrentCarImageUri.invoke())
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
        icon = painterResource(com.andef.mycarandef.design.R.drawable.works),
        contentDescription = "Иконка работы",
        title = "Работы",
        route = Screen.MainScreens.WorksMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(com.andef.mycarandef.design.R.drawable.ruble),
        contentDescription = "Иконка рубля",
        title = "Траты",
        Screen.MainScreens.ExpensesMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(com.andef.mycarandef.design.R.drawable.location),
        contentDescription = "Иконка локация значок",
        title = "Карта",
        Screen.MainScreens.MapsMainScreen.route
    ),
    UiNavigationBarItem(
        icon = painterResource(com.andef.mycarandef.design.R.drawable.car),
        contentDescription = "Иконка машины",
        title = "Гараж",
        Screen.MainScreens.CarsMainScreen.route
    )
)