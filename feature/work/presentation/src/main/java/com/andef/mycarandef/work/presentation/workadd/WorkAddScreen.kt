package com.andef.mycarandef.work.presentation.workadd

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.chooser.ui.UiChooser
import com.andef.mycarandef.design.datepicker.ui.UiDatePickerDialog
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.utils.MileageVisualTransformation
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkAddScreen(
    workId: Long?,
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    carId: Long
) {
    val viewModel: WorkAddViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        workId?.let {
            viewModel.send(
                WorkAddIntent.InitWorkByLateWork(
                    workId = workId,
                    onError = { msg ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true
                            )
                            navHostController.popBackStack()
                        }
                    }
                )
            )
        }
    }

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.Center,
                title = "Работы",
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
                type = UiSnackbarType.Error
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
                scrollState = scrollState,
                state = state,
                viewModel = viewModel
            )
            DownButton(
                isLightTheme = isLightTheme,
                keyboard = keyboard,
                viewModel = viewModel,
                state = state,
                navHostController = navHostController,
                scope = scope,
                snackbarHostState = snackbarHostState,
                carId = carId
            )
        }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiDatePickerDialog(
        isVisible = state.value.datePickerVisible,
        isLightTheme = isLightTheme,
        onDismissRequest = { viewModel.send(WorkAddIntent.ChangeDatePickerVisible(false)) },
        onOkClick = { date ->
            viewModel.send(WorkAddIntent.ChangeDate(date))
            viewModel.send(WorkAddIntent.ChangeDatePickerVisible(false))
        }
    )
    BackHandler { if (!state.value.isLoading) navHostController.popBackStack() }
}

@Composable
private fun ColumnScope.MainContent(
    isLightTheme: Boolean,
    scrollState: ScrollState,
    state: State<WorkAddState>,
    viewModel: WorkAddViewModel
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Image(
            modifier = Modifier
                .padding(top = 12.dp)
                .size(130.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f)
                ),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.my_car_clipboard_works),
            contentDescription = "Иконка блокнот"
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Обязательные поля:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = grayColor(isLightTheme),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.workTitle,
            onValueChange = { viewModel.send(WorkAddIntent.ChangeWorkTitle(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Выполненная работа",
            leadingIcon = painterResource(R.drawable.my_car_works),
            contentDescription = "Значок работы",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.mileage?.toString() ?: "",
            onValueChange = {
                try {
                    val mileage = it.toInt()
                    viewModel.send(WorkAddIntent.ChangeMileage(mileage))
                } catch (_: Exception) {
                    viewModel.send(WorkAddIntent.ChangeMileage(null))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Пробег (км)",
            leadingIcon = painterResource(R.drawable.my_car_car),
            contentDescription = "Значок машины",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next
            ),
            visualTransformation = MileageVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiChooser(
            isLightTheme = isLightTheme,
            value = state.value.date?.let { formatLocalDate(it) } ?: "",
            onClick = { viewModel.send(WorkAddIntent.ChangeDatePickerVisible(true)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Дата",
            leadingIcon = painterResource(R.drawable.my_car_schedule),
            leadingIconContentDescription = "Значок часов",
            trailingIcon = painterResource(R.drawable.my_car_calendar),
            trailingIconContentDescription = "Значок календаря"
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Необязательные поля:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = grayColor(isLightTheme),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.note?.toString() ?: "",
            onValueChange = { viewModel.send(WorkAddIntent.ChangeNote(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            placeholderText = "Примечание",
            leadingIcon = painterResource(R.drawable.my_car_comment),
            contentDescription = "Значок комментария",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun ColumnScope.DownButton(
    isLightTheme: Boolean,
    keyboard: SoftwareKeyboardController?,
    viewModel: WorkAddViewModel,
    navHostController: NavHostController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    state: State<WorkAddState>,
    carId: Long
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiButton(
            text = "Сохранить",
            onClick = {
                keyboard?.hide()
                viewModel.send(
                    WorkAddIntent.SaveClick(
                        onSuccess = navHostController::popBackStack,
                        onError = { msg ->
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = msg,
                                    withDismissAction = true
                                )
                            }
                        },
                        carId = carId
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .imePadding(),
            enabled = state.value.saveButtonEnabled && !state.value.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}