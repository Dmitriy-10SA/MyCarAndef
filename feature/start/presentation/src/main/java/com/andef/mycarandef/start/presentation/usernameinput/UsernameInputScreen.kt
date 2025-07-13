package com.andef.mycarandef.start.presentation.usernameinput

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun UsernameInputScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    val viewModel: UsernameInputViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val keyboard = LocalSoftwareKeyboardController.current

    UiScaffold(
        isLightTheme = isLightTheme,
        snackbarHost = {
            UiSnackbar(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                type = UiSnackbarType.Error
            )
        }
    ) {
        MainContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .navigationBarsPadding()
                .padding(horizontal = 12.dp)
                .imePadding()
                .verticalScroll(scrollState),
            viewModel = viewModel,
            state = state,
            navHostController = navHostController,
            snackbarHostState = snackbarHostState,
            isLightTheme = isLightTheme,
            scope = scope,
            keyboard = keyboard
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier,
    navHostController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: UsernameInputViewModel,
    state: State<UsernameInputState>,
    scope: CoroutineScope,
    keyboard: SoftwareKeyboardController?,
    isLightTheme: Boolean
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "MyCar",
            fontFamily = FontFamily(Font(R.font.dm_sans)),
            fontSize = 40.sp,
            color = if (isLightTheme) Black else White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.username,
            onValueChange = { viewModel.send(UsernameInputIntent.UsernameChange(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Ваше имя",
            leadingIcon = painterResource(R.drawable.person),
            contentDescription = "Иконка человечка",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiButton(
            text = "Продолжить", onClick = {
                keyboard?.hide()
                viewModel.send(
                    UsernameInputIntent.NextClick(
                        onSuccess = { route ->
                            navHostController.navigate(route) { popUpTo(0) }
                        },
                        onError = { msg ->
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = msg,
                                    withDismissAction = true
                                )
                            }
                        })
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.value.nextButtonEnabled
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}