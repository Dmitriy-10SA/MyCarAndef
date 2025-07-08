package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}