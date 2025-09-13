package com.andef.mycarandef.design.loading.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.dialog.container.ui.UiDialogContainer
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import kotlinx.coroutines.delay

@Composable
fun UiLoading(
    isVisible: Boolean,
    isLightTheme: Boolean
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(350)
            visible = isVisible
        } else {
            visible = false
        }
    }

    if (visible) {
        UiDialogContainer(isLightTheme = isLightTheme, onDismissRequest = null) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(R.drawable.my_car_app_icon),
                    contentDescription = "Иконка приложения",
                    tint = blackOrWhiteColor(isLightTheme)
                )
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    modifier = Modifier.width(140.dp),
                    color = GreenColor,
                    trackColor = blackOrWhiteColor(isLightTheme)
                )
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    }
}