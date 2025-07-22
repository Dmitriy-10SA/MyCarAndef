package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import javax.inject.Inject

class GetExpensesByCarIdAsListUseCase @Inject constructor(private val repository: ExpenseRepository) {
    suspend operator fun invoke(carId: Long) = repository.getExpensesByCarIdAsList(carId)
}