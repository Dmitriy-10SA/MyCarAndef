package com.andef.mycarandef.work.presentation.workmain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.work.ui.UiWorkCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.viewmodel.ViewModelFactory

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
    if (state.value.showBottomSheet) {
        BottomSheet(
            navHostController = navHostController,
            viewModel = viewModel,
            sheetState = sheetState,
            isLightTheme = isLightTheme
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    navHostController: NavHostController,
    viewModel: WorkMainViewModel,
    sheetState: SheetState,
    isLightTheme: Boolean
) {
    UiModalBottomSheet (
        onDismissRequest = { viewModel.send(WorkMainIntent.BottomSheetVisibleChange(false)) },
        sheetState = sheetState,
        isLightTheme = isLightTheme
    ) {

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
                onClick = { viewModel.send(WorkMainIntent.BottomSheetVisibleChange(true)) },
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