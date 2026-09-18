package com.andef.mycarandef.expense.domain.entities

import java.time.LocalDate

data class Expense(
    val id: Long,
    val amount: Long,
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
        val allExpenseTypes = ExpenseType.entries.toList()
    }
}

enum class ExpenseType(val title: String) {
    FUEL(title = "Бензин"),
    WORKS(title = "Работы"),
    PARTS(title = "Запчасти"),
    WASHING(title = "Мойка"),
    PARKING(title = "Парковки"),
    TOLL_ROADS(title = "Платные дороги"),
    FINES(title = "Штрафы"),
    INSURANCE(title = "Страховка"),
    TAXES(title = "Налоги"),
    OTHER(title = "Другое")
}
