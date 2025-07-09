package com.andef.mycarandef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.graph.MyCarNavGraph

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyCarApp).component }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        //TODO ("Пока делаем светлую тему, потом темную")
        setContent {
            val navHostController = rememberNavController()
            MyCarAndefTheme(darkTheme = false) {
                MyCarNavGraph(
                    navHostController = navHostController,
                    viewModelFactory = component.viewModelFactory,
                    paddingValues = PaddingValues(0.dp),
                    isFirstStart = component.getIsFirstStartUseCase.invoke()
                )
            }
        }
    }
}