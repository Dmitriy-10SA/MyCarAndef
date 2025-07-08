package com.andef.mycarandef.expense.domain.repository

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    suspend fun changeExpense(
        id: Long,
        amount: Double,
        note: String?,
        type: ExpenseType,
        date: LocalDate
    )

    suspend fun removeExpense(id: Long)
    suspend fun getExpenseById(id: Long): Expense
    fun getExpensesByCarId(carId: Int): Flow<List<Expense>>
}