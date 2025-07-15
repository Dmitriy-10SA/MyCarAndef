package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesByCarIdUseCase @Inject constructor(private val repository: ExpenseRepository) {
    operator fun invoke(carId: Long): Flow<List<Expense>> = repository.getExpensesByCarId(carId)
}