package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import java.time.LocalDate
import javax.inject.Inject

class ChangeExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(
        id: Long,
        amount: Double,
        note: String?,
        type: ExpenseType,
        date: LocalDate
    ) = repository.changeExpense(id, amount, note, type, date)
}