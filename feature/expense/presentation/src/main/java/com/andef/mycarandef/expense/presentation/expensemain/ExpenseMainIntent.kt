package com.andef.mycarandef.expense.presentation.expensemain

import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

sealed class ExpenseMainIntent {
    data class SubscribeForExpenses(val currentCarId: Long) : ExpenseMainIntent()
    data class BottomSheetVisibleChange(
        val isVisible: Boolean,
        val expenseDate: LocalDate? = null,
        val expenseType: ExpenseType? = null,
        val expenseAmount: Double? = null,
        val expenseId: Long? = null,
        val carId: Long? = null
    ) : ExpenseMainIntent()

    data class DeleteExpense(
        val expenseId: Long,
        val onError: (String) -> Unit
    ) : ExpenseMainIntent()

    data class ChangeDeleteDialogVisible(val isVisible: Boolean) : ExpenseMainIntent()
}