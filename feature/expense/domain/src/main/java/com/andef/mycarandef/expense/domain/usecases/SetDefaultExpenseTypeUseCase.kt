package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class SetDefaultExpenseTypeUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(type: ExpenseType?) = repository.setDefaultExpenseType(type)
}
