package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetDefaultExpenseTypeUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke() = repository.getDefaultExpenseType()
}
