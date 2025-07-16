package com.andef.mycarandef.expense.presentation.expenseadd

import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

data class ExpenseAddState(
    val expenseId: Long? = null,
    val amount: Double? = null,
    val type: ExpenseType? = null,
    val date: LocalDate? = null,
    val note: String? = null,
    val isLoading: Boolean = false,
    val datePickerVisible: Boolean = false,
    val saveButtonEnabled: Boolean = false,
    val isAdd: Boolean = true
)
