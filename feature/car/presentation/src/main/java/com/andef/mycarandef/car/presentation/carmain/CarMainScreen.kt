package com.andef.mycarandef.car.presentation.carmain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.card.car.ui.UiCarCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.viewmodel.ViewModelFactory

@Composable
fun CarMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    val viewModel: CarMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.send(CarMainIntent.GetCars) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(0.dp)) }
        items(items = state.value.cars, key = { it.id }) { car ->
            UiCarCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { TODO() },
                isLightTheme = isLightTheme,
                car = car,
                context = context
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
        onRetry = { viewModel.send(CarMainIntent.GetCars) }
    )
}