package com.andef.mycarandef.expense.data.mapper

import com.andef.mycarandef.expense.data.dbo.ExpenseDbo
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.utils.toInt
import com.andef.mycarandef.utils.toLocalDate
import javax.inject.Inject

class ExpenseMapper @Inject constructor() {
    fun map(expense: Expense): ExpenseDbo = ExpenseDbo(
        id = expense.id,
        amount = expense.amount,
        note = expense.note,
        type = expense.type,
        date = expense.date.toInt(),
        carId = expense.carId
    )

    fun map(expenseDbo: ExpenseDbo): Expense = Expense(
        id = expenseDbo.id,
        amount = expenseDbo.amount,
        note = expenseDbo.note,
        type = expenseDbo.type,
        date = expenseDbo.date.toLocalDate(),
        carId = expenseDbo.carId
    )
}