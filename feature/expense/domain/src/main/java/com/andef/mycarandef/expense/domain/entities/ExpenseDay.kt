package com.andef.mycarandef.expense.domain.entities

import java.time.LocalDate

data class ExpenseDay(
    val date: LocalDate,
    val items: List<Expense>
)
