package com.andef.mycarandef.expense.presentation.expensemain

sealed class ExpenseMainIntent {
    data class SubscribeForExpenses(val currentCarId: Long) : ExpenseMainIntent()
}