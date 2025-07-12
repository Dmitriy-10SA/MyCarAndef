package com.andef.mycarandef.start.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.viewmodel.ViewModelFactory

@Composable
fun UsernameInputScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    val viewModel: UsernameInputViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .navigationBarsPadding()
            .padding(horizontal = 12.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
        }
        item {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "MyCar",
                fontSize = 40.sp,
                color = if (isLightTheme) Black else White,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            UiTextField(
                isLightTheme = isLightTheme,
                value = state.value.username,
                onValueChange = { viewModel.send(UsernameInputIntent.UsernameChange(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholderText = "Ваше имя",
                leadingIcon = painterResource(com.andef.mycarandef.design.R.drawable.person),
                contentDescription = "Иконка человечка",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            UiButton(
                text = "Продолжить",
                onClick = {
                    viewModel.send(UsernameInputIntent.NextClick(onSuccess = { route ->
                        navHostController.navigate(route)
                    }))
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.nextButtonEnabled
            )
        }
        item {
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}