package com.andef.mycarandef.expense.domain.repository

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    suspend fun getAllExpensesAsList(): List<Expense>
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
    suspend fun getExpensesByCarIdAsList(carId: Long): List<Expense>
    fun getExpensesByCarId(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Expense>>
}