package com.andef.mycarandef.expense.presentation.expensemain

import com.andef.mycarandef.expense.domain.entities.Expense

data class ExpenseMainState(
    val works: List<Expense> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
