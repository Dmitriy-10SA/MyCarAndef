package com.andef.mycar.backup.presentation.backupstart

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.backup.presentation.gson
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.loading.ui.UiLoading
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val feedbackSheetVisible = remember { mutableStateOf(false) }
    val feedbackSheetState = rememberModalBottomSheetState()
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
                title = "Восстановление данных",
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
            MainContent(isLightTheme = isLightTheme, launcher = launcher)
            DownText(
                isLightTheme = isLightTheme,
                context = context,
                feedbackSheetVisible = feedbackSheetVisible,
                feedbackSheetState = feedbackSheetState
            )
        }
    }
    UiLoading(
        isLightTheme = isLightTheme,
        isVisible = state.value.isLoading,
        paddingValues = paddingValues,
        withTouch = false
    )
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
    launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.DownText(
    isLightTheme: Boolean,
    context: Context,
    feedbackSheetVisible: MutableState<Boolean>,
    feedbackSheetState: SheetState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Нужна помощь?",
            color = if (isLightTheme) GrayForLight else GrayForDark,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { feedbackSheetVisible.value = true })
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    UiModalBottomSheet(
        isLightTheme = isLightTheme,
        isVisible = feedbackSheetVisible.value,
        onDismissRequest = { feedbackSheetVisible.value = false },
        sheetState = feedbackSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Нужна помощь? Напишите разработчику:",
                color = if (isLightTheme) GrayForLight else GrayForDark,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(R.drawable.my_car_telegram_icon),
                    contentDescription = "Иконка телеграмм",
                    text = "Telegram",
                    onClick = {
                        Intent(Intent.ACTION_VIEW, "https://t.me/dsemkin".toUri()).apply {
                            context.startActivity(this)
                        }
                    }
                )
                AppItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(R.drawable.my_car_mail_icon),
                    contentDescription = "Иконка почты",
                    text = "Mail",
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:semkin_dmitriy10@vk.com".toUri()
                        }
                        context.startActivity(
                            Intent.createChooser(
                                emailIntent,
                                "Выберите почтовый клиент"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.AppItem(
    isLightTheme: Boolean,
    icon: Painter,
    contentDescription: String,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .size(65.dp)
                .background(color = White, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = if (isLightTheme) {
                        GrayForLight.copy(alpha = 0.3f)
                    } else {
                        GrayForDark.copy(alpha = 0.3f)
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(all = 7.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = text,
            color = if (isLightTheme) Black else White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
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