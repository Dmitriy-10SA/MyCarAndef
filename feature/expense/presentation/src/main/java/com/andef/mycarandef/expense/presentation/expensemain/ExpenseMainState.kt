package com.andef.mycarandef.expense.presentation.expensemain

import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

data class ExpenseMainState(
    val expenses: List<Expense> = listOf(),
    val showBottomSheet: Boolean = false,
    val expenseTypeInBottomSheet: ExpenseType? = null,
    val expenseDateInBottomSheet: LocalDate? = null,
    val expenseIdInBottomSheet: Long? = null,
    val carIdForExpenseBottomSheet: Long? = null,
    val deleteDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
