package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetAllExpensesAsListUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke() = repository.getAllExpensesAsList()
}