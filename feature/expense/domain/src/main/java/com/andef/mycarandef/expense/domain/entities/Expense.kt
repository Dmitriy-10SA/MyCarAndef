package com.andef.mycarandef.expense.domain.entities

import java.time.LocalDate

data class Expense(
    val id: Long,
    val amount: Double,
    val note: String?,
    val type: ExpenseType,
    val date: LocalDate,
    val carId: Int
)

enum class ExpenseType(val title: String) {
    FUEL(title = "Бензин"),
    WORKS(title = "Работы"),
    WASHING(title = "Мойка"),
    OTHER(title = "Другое")
}