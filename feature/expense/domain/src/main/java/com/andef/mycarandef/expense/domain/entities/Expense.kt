package com.andef.mycarandef.expense.domain.entities

import com.andef.mycarandef.expense.domain.entities.ExpenseType.FUEL
import com.andef.mycarandef.expense.domain.entities.ExpenseType.OTHER
import com.andef.mycarandef.expense.domain.entities.ExpenseType.WASHING
import com.andef.mycarandef.expense.domain.entities.ExpenseType.WORKS
import java.time.LocalDate

data class Expense(
    val id: Long,
    val amount: Double,
    val note: String?,
    val type: ExpenseType,
    val date: LocalDate,
    val carId: Long
) : Comparable<Expense> {
    override fun compareTo(other: Expense): Int {
        return if (this.date > other.date) -1
        else if (this.date == other.date) 0
        else 1
    }

    companion object {
        val allExpenseTypes = listOf<ExpenseType>(FUEL, WORKS, WASHING, OTHER)
    }
}

enum class ExpenseType(val title: String) {
    FUEL(title = "Бензин"),
    WORKS(title = "Работы"),
    WASHING(title = "Мойка"),
    OTHER(title = "Другое")
}