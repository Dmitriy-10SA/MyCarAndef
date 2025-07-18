package com.andef.mycarandef.map.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.viewmodel.ViewModelFactory

@Composable
fun MapMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    val viewModel: MapMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()
}