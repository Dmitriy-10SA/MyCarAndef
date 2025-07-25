package com.andef.mycar.backup.presentation.backupstart

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.backup.presentation.gson
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun BackupStartScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    val viewModel: BackupStartViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                val backupData = importDataFromJson(it, context)
                if (backupData != null) {
                    viewModel.send(
                        BackupStartIntent.RestoreDate(
                            data = backupData,
                            onSuccess = { msg ->
                                navHostController.navigate(
                                    Screen.MainScreens.WorksMainScreen.route
                                ) { popUpTo(0) }
                            },
                            onError = { msg ->
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = msg,
                                        withDismissAction = true
                                    )
                                }
                            }
                        )
                    )
                } else {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = "Не удалось восстановить данные. Убедитесь, что выбран правильный файл.",
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    )

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.Center,
                title = "Резервное копирование",
                navigationIcon = painterResource(R.drawable.my_car_arrow_back),
                navigationIconContentDescription = "Назад",
                onNavigationIconClick = {
                    if (!state.value.isLoading) navHostController.popBackStack()
                }
            )
        },
        snackbarHost = {
            UiSnackbar(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                type = if (state.value.isErrorSnackbar) {
                    UiSnackbarType.Error
                } else {
                    UiSnackbarType.Success
                }
            )
        }
    ) { topBarPadding ->
        MainContent(topBarPadding = topBarPadding, isLightTheme = isLightTheme, launcher = launcher)
    }
    BackHandler {
        if (!state.value.isLoading) {
            navHostController.popBackStack()
        } else {
            Toast.makeText(
                context,
                "Подождите, идет загрузка!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
private fun MainContent(
    topBarPadding: PaddingValues,
    isLightTheme: Boolean,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = topBarPadding.calculateTopPadding())
            .padding(horizontal = 12.dp)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Уже пользовались MyCar?",
            fontSize = 20.sp,
            color = if (isLightTheme) Black else White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Загрузите резервную копию, чтобы восстановить данные с другого устройства",
            fontSize = 14.sp,
            color = if (isLightTheme) GrayForLight else GrayForDark,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        UiButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Восстановить данные",
            onClick = { launcher.launch(arrayOf("application/json")) }
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}


private fun importDataFromJson(uri: Uri, context: Context): BackupData? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val json = inputStream?.bufferedReader().use { it?.readText() }
        json?.let { gson.fromJson(it, BackupData::class.java) }
    } catch (_: Exception) {
        null
    }
}