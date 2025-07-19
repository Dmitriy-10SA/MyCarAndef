package com.andef.mycarandef.work.presentation.workmain

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.work.ui.UiWorkCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.Red
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatMileage
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    val viewModel: WorkMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(currentCarId) { viewModel.send(WorkMainIntent.SubscribeForWorks(currentCarId)) }

    MainContent(
        viewModel = viewModel,
        state = state,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        currentCarId = currentCarId
    )
    BottomSheetWithDeleteDialog(
        navHostController = navHostController,
        viewModel = viewModel,
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        state = state,
        scope = scope,
        snackbarHostState = snackbarHostState
    )
    UiSnackbar(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        type = UiSnackbarType.Error
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetWithDeleteDialog(
    navHostController: NavHostController,
    viewModel: WorkMainViewModel,
    sheetState: SheetState,
    isLightTheme: Boolean,
    state: State<WorkMainState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    state.value.workIdInBottomSheet?.let { workId ->
        state.value.workTitleInBottomSheet?.let { workTitle ->
            state.value.workDateInBottomSheet?.let { workDate ->
                state.value.mileageInBottomSheet?.let { workMileage ->
                    state.value.carIdForWorkBottomSheet?.let { carId ->
                        UiModalBottomSheet(
                            onDismissRequest = {
                                viewModel.send(
                                    WorkMainIntent.BottomSheetVisibleChange(isVisible = false)
                                )
                            },
                            sheetState = sheetState,
                            isLightTheme = isLightTheme,
                            isVisible = state.value.showBottomSheet
                        ) {
                            BottomSheetContent(
                                isLightTheme = isLightTheme,
                                workTitle = workTitle,
                                workDate = workDate,
                                onDeleteClick = {
                                    viewModel.send(
                                        WorkMainIntent.ChangeDeleteDialogVisible(isVisible = true)
                                    )
                                },
                                mileage = workMileage,
                                onEditClick = {
                                    viewModel.send(
                                        WorkMainIntent.BottomSheetVisibleChange(isVisible = false)
                                    )
                                    navHostController.navigate(
                                        Screen.WorkScreen.passId(id = workId, carId = carId)
                                    )
                                }
                            )
                        }
                        DeleteDialog(
                            isLightTheme = isLightTheme,
                            workId = workId,
                            viewModel = viewModel,
                            scope = scope,
                            state = state,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeleteDialog(
    workId: Long,
    isLightTheme: Boolean,
    viewModel: WorkMainViewModel,
    state: State<WorkMainState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Вы уверены?",
        onDismissRequest = {
            viewModel.send(
                WorkMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        onYesClick = {
            viewModel.send(
                WorkMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
            viewModel.send(
                WorkMainIntent.BottomSheetVisibleChange(isVisible = false)
            )
            viewModel.send(
                WorkMainIntent.DeleteWork(
                    workId = workId,
                    onError = { msg ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true
                            )
                        }
                    }
                )
            )
        },
        onCancelClick = {
            viewModel.send(
                WorkMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        isVisible = state.value.deleteDialogVisible
    )
}

@Composable
private fun BottomSheetContent(
    isLightTheme: Boolean,
    workTitle: String,
    workDate: LocalDate,
    mileage: Int,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Text(text = workTitle, fontSize = 16.sp, color = if (isLightTheme) Black else White)
            Text(
                text = "${formatLocalDate(workDate)} - ${formatMileage(mileage)}",
                fontSize = 14.sp,
                color = if (isLightTheme) GrayForLight else GrayForDark
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onEditClick)
        ) {
            Icon(
                painter = painterResource(R.drawable.my_car_edit),
                tint = if (isLightTheme) Black else White,
                contentDescription = "Карандаш (изменить)"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Изменить", color = if (isLightTheme) Black else White, fontSize = 16.sp)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onDeleteClick)
        ) {
            Icon(
                painter = painterResource(R.drawable.my_car_delete),
                tint = Red,
                contentDescription = "Корзина"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Удалить", color = Red, fontSize = 16.sp)
        }
    }
}

@Composable
private fun MainContent(
    viewModel: WorkMainViewModel,
    state: State<WorkMainState>,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(0.dp)) }
        items(items = state.value.works, key = { it.id }) { work ->
            UiWorkCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                onClick = {
                    viewModel.send(
                        WorkMainIntent.BottomSheetVisibleChange(
                            isVisible = true,
                            workTitle = work.title,
                            workMileage = work.mileage,
                            workDate = work.date,
                            workId = work.id,
                            carId = work.carId
                        )
                    )
                },
                isLightTheme = isLightTheme,
                work = work
            )
        }
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
    UiLoading(
        isVisible = state.value.isLoading,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme
    )
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = { viewModel.send(WorkMainIntent.SubscribeForWorks(currentCarId)) }
    )
}