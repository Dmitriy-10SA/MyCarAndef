package com.andef.mycarandef.expense.domain.usecases

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate
import javax.inject.Inject

class GetExpensesByCarIdUseCase @Inject constructor(private val repository: ExpenseRepository) {
    operator fun invoke(
        carId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<Expense>> =
        repository
            .getExpensesByCarId(carId, startDate, endDate)
            .flowOn(Dispatchers.IO)
}