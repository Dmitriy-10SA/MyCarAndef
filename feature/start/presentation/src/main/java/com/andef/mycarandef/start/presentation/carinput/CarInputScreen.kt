package com.andef.mycarandef.start.presentation.carinput

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.viewmodel.ViewModelFactory

@Composable
fun CarInputScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean
) {
    val viewModel: CarInputViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()
}