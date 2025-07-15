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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.work.ui.UiWorkCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.Red
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.viewmodel.ViewModelFactory
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

    LaunchedEffect(Unit) { viewModel.send(WorkMainIntent.SubscribeForWorks(currentCarId)) }

    MainContent(
        viewModel = viewModel,
        state = state,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        currentCarId = currentCarId
    )
    BottomSheet(
        navHostController = navHostController,
        viewModel = viewModel,
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    navHostController: NavHostController,
    viewModel: WorkMainViewModel,
    sheetState: SheetState,
    isLightTheme: Boolean,
    state: State<WorkMainState>
) {
    state.value.workIdInBottomSheet?.let { workId ->
        state.value.workTitleInBottomSheet?.let { workTitle ->
            state.value.workDateInBottomSheet?.let { workDate ->
                UiModalBottomSheet(
                    onDismissRequest = {
                        viewModel.send(WorkMainIntent.BottomSheetVisibleChange(isVisible = false))
                    },
                    sheetState = sheetState,
                    isLightTheme = isLightTheme,
                    isVisible = state.value.showBottomSheet
                ) {
                    BottomSheetContent(
                        isLightTheme = isLightTheme,
                        workTitle = workTitle,
                        workDate = workDate,
                        onDeleteClick = { TODO() },
                        onEditClick = { TODO() }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomSheetContent(
    isLightTheme: Boolean,
    workTitle: String,
    workDate: LocalDate,
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
            Text(
                text = workTitle,
                fontSize = 16.sp,
                color = if (isLightTheme) Black else White
            )
            Text(
                text = formatLocalDate(workDate),
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
                painter = painterResource(R.drawable.edit),
                tint = if (isLightTheme) Black else White,
                contentDescription = "Карандаш (изменить)"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Изменить",
                color = if (isLightTheme) Black else White,
                fontSize = 16.sp
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onDeleteClick)
        ) {
            Icon(
                painter = painterResource(R.drawable.delete),
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
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.send(
                        WorkMainIntent.BottomSheetVisibleChange(
                            isVisible = true,
                            workTitle = work.title,
                            workDate = work.date,
                            workId = work.id
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