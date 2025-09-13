package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id: Long) = repository.getExpenseById(id)
}