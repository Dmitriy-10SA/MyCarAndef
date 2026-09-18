package com.andef.mycarandef.expense.domain.entities

import java.time.LocalDate

data class ExpenseForLazyColumn(
    val date: LocalDate,
    val totalAmount: Long,
    val expenses: List<Expense>
)
