package com.andef.mycarandef.expense.presentation.expenseadd

import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

sealed class ExpenseAddIntent {
    data class ChangeAmount(val amount: Double?) : ExpenseAddIntent()
    data class ChangeType(val type: ExpenseType) : ExpenseAddIntent()
    data class ChangeNote(val note: String?) : ExpenseAddIntent()
    data class ChangeDate(val date: LocalDate) : ExpenseAddIntent()
    data class ChangeDatePickerVisible(val isVisible: Boolean) : ExpenseAddIntent()
    data class SaveClick(
        val carId: Long,
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    ) : ExpenseAddIntent()

    data class InitExpenseByLateExpense(
        val expenseId: Long,
        val onError: (String) -> Unit
    ) : ExpenseAddIntent()
}