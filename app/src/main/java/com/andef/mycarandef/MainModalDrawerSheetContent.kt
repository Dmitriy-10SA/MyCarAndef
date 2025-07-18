package com.andef.mycarandef

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.White
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainModalDrawerSheetContent(
    isLightTheme: Boolean,
    drawerState: DrawerState,
    scope: CoroutineScope,
    component: MyCarComponent
) {
    ModalDrawerSheet(
        drawerState = drawerState,
        drawerShape = RoundedCornerShape(
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp,
            topStart = 0.dp
        ),
        drawerContainerColor = if (isLightTheme) White else DarkGray,
        drawerContentColor = if (isLightTheme) Black else White
    ) {
        InnerContent(
            isLightTheme = isLightTheme,
            drawerState = drawerState,
            scope = scope,
            component = component
        )
    }
}

@Composable
private fun InnerContent(
    isLightTheme: Boolean,
    drawerState: DrawerState,
    scope: CoroutineScope,
    component: MyCarComponent
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            tint = if (isLightTheme) Black else White,
            painter = painterResource(com.andef.mycarandef.design.R.drawable.car_key),
            contentDescription = "Иконка приложения"
        )
    }
}