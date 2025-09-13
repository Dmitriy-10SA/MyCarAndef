package com.andef.mycar.backup.presentation.backupmain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycar.backup.presentation.DownText
import com.andef.mycar.backup.presentation.gson
import com.andef.mycar.backup.presentation.importDataFromJson
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
) {
    val viewModel: BackupMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val feedbackSheetVisible = remember { mutableStateOf(false) }
    val feedbackSheetState = rememberModalBottomSheetState()
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
                        BackupMainIntent.RestoreDate(
                            data = backupData,
                            onSuccess = { msg ->
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = msg,
                                        withDismissAction = true
                                    )
                                }
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
                navigationIconTint = GreenColor,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarPadding.calculateTopPadding())
                .navigationBarsPadding()
                .imePadding()
        ) {
            MainContent(
                isLightTheme = isLightTheme,
                viewModel = viewModel,
                scope = scope,
                snackbarHostState = snackbarHostState,
                context = context,
                launcher = launcher
            )
            DownText(
                isLightTheme = isLightTheme,
                context = context,
                feedbackSheetVisible = feedbackSheetVisible,
                feedbackSheetState = feedbackSheetState
            )
        }
    }
    UiLoading(isLightTheme = isLightTheme, isVisible = state.value.isLoading)
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
private fun ColumnScope.MainContent(
    isLightTheme: Boolean,
    viewModel: BackupMainViewModel,
    scope: CoroutineScope,
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Не теряйте данные о машинах",
            fontSize = 20.sp,
            color = blackOrWhiteColor(isLightTheme),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Сохраните резервную копию, чтобы восстановить информацию о машинах при смене " +
                    "телефона или очистке данных приложения. Восстанавливать данные не нужно, " +
                    "если вы не меняли телефон и не очищали данные, иначе текущие данные могут " +
                    "быть удалены",
            fontSize = 14.sp,
            color = grayColor(isLightTheme),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        UiButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Сохранить копию",
            onClick = {
                viewModel.send(
                    BackupMainIntent.SaveData(
                        onSuccess = { backupData ->
                            try {
                                val json = gson.toJson(backupData)
                                val fileName = "Мои_авто_резервная_копия_${LocalDate.now()}.json"
                                val file = File(context.cacheDir, fileName)
                                file.writeText(json)

                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    file
                                )
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "application/json"
                                    putExtra(Intent.EXTRA_STREAM, uri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        "Поделиться резервной копией"
                                    )
                                )
                            } catch (e: Exception) {
                                Log.e("BackupViewModel", e.toString())
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = "Ошибка! Попробуйте ещё раз!",
                                        withDismissAction = true
                                    )
                                }
                            }
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
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        UiButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Восстановить данные",
            onClick = { launcher.launch(arrayOf("application/json")) }
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}