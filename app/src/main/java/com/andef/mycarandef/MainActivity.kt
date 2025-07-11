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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.chooser.ui.UiChooser
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.MyCarAndefTheme
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
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
//        MyCarNavGraph(
//            navHostController = navHostController,
//            viewModelFactory = component.viewModelFactory,
//            paddingValues = PaddingValues(0.dp),
//            isFirstStart = component.getIsFirstStartUseCase()
//        )
        var value1 by rememberSaveable { mutableStateOf("") }
        var value2 by rememberSaveable { mutableStateOf("") }
        UiScaffold(
            isLightTheme = isLightTheme,
            topBar = {
                UiTopBar(
                    isLightTheme = isLightTheme,
                    type = UiTopBarType.NotCenter,
                    title = "Привет, Дмитрий!",
                    navigationIcon = painterResource(com.andef.mycarandef.design.R.drawable.menu),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(com.andef.mycarandef.design.R.drawable.attach),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UiTextField(
                    isLightTheme = isLightTheme,
                    value = value1,
                    onValueChange = { value1 = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholderText = "Ваше имя",
                    contentDescription = "",
                    leadingIcon = painterResource(com.andef.mycarandef.design.R.drawable.person)
                )
                val context = LocalContext.current
                UiChooser(
                    onClick = {
                        value2 = "image-12r4124123124-wefsdsf12=3=124124dsf"
                    },
                    isLightTheme = isLightTheme,
                    value = value2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholderText = "Ваше имя",
                    leadingIconContentDescription = "",
                    leadingIcon = painterResource(com.andef.mycarandef.design.R.drawable.image),
                    trailingIcon = painterResource(com.andef.mycarandef.design.R.drawable.attach),
                    trailingIconContentDescription = ""
                )
                UiButton(
                    text = "Продолжить",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}