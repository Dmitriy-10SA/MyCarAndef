package com.andef.mycarandef.expense.presentation.expenseanalysis

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ExpenseAnalysisViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ExpenseAnalysisState())
    val state: StateFlow<ExpenseAnalysisState> = _state

    fun send(intent: ExpenseAnalysisIntent) {

    }
}