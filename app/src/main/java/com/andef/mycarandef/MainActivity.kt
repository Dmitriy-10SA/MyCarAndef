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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.theme.MyCarAndefTheme

class MainActivity : ComponentActivity() {
    private val component by lazy { (application as MyCarApp).component }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            MainContent(navHostController = navHostController, component = component)
        }
    }
}

@Composable
private fun MainContent(navHostController: NavHostController, component: MyCarComponent) {
    MyCarAndefTheme(darkTheme = !component.getIsLightThemeUseCase(isSystemInDarkTheme())) {
//        MyCarNavGraph(
//            navHostController = navHostController,
//            viewModelFactory = component.viewModelFactory,
//            paddingValues = PaddingValues(0.dp),
//            isFirstStart = component.getIsFirstStartUseCase()
//        )
        var enabled by rememberSaveable { mutableStateOf(true) }
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UiButton(
                    text = "Продолжить",
                    onClick = { enabled = !enabled },
                    modifier = Modifier
                        //.fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    enabled = enabled
                )
            }
        }
    }
}