package com.andef.mycar.backup.presentation.backupstart

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.backup.presentation.gson
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun BackupStartScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    //navHostController.navigate(Screen.MainScreens.WorksMainScreen.route) { popUpTo(0) }
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