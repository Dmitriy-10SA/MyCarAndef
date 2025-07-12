package com.andef.mycarandef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.graph.MyCarNavGraph
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
        MyCarNavGraph(
            navHostController = navHostController,
            viewModelFactory = component.viewModelFactory,
            paddingValues = PaddingValues(0.dp),
            isFirstStart = component.getIsFirstStartUseCase(),
            isLightTheme = isLightTheme
        )
    }
}