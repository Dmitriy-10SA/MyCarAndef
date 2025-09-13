package com.andef.mycarandef.expense.presentation.expenseanalysis

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

data class ExpenseAnalysisState(
    val totalSumForScreen: Double? = null,
    val expensesInfoForScreen: Map<ExpenseType, Pair<Float, Double>> = mapOf<ExpenseType, Pair<Float, Double>>(),
    val expenses: List<Expense> = listOf<Expense>(),
    val selectedDateTabId: Int = 0,
    val lastSelectedDateTabId: Int = 0,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val dateRangePickerVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
