package com.andef.mycarandef.expense.presentation.expenseanalysis

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.viewmodel.ViewModelFactory

@Composable
fun ExpenseAnalysisScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    carId: Long
) {
    val viewModel: ExpenseAnalysisViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()
}