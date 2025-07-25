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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
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
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BackupMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
) {
    val viewModel: BackupMainViewModel = viewModel(factory = viewModelFactory)
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
        MainContent(
            topBarPadding = topBarPadding,
            isLightTheme = isLightTheme,
            viewModel = viewModel,
            scope = scope,
            snackbarHostState = snackbarHostState,
            context = context,
            launcher = launcher
        )
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


private class LocalDateAdapter : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate {
        return LocalDate.parse(json?.asString, formatter)
    }
}

private class LocalTimeAdapter : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME
    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?) =
        JsonPrimitive(src?.format(formatter))

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ) =
        LocalTime.parse(json?.asString, formatter)
}

private val gson = GsonBuilder()
    .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
    .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
    .create()

@Composable
private fun MainContent(
    topBarPadding: PaddingValues,
    isLightTheme: Boolean,
    viewModel: BackupMainViewModel,
    scope: CoroutineScope,
    context: Context,
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    snackbarHostState: SnackbarHostState
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
        Text(
            text = "Не теряйте данные о машинах",
            fontSize = 20.sp,
            color = if (isLightTheme) Black else White,
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
            color = if (isLightTheme) GrayForLight else GrayForDark,
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
                                val fileName = "MyCar_резервная_копия_${LocalDate.now()}.json"
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