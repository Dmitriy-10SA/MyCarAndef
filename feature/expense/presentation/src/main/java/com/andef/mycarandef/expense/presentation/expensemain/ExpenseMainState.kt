package com.andef.mycarandef.expense.presentation.expensemain

import com.andef.mycarandef.expense.domain.entities.ExpenseDay
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

data class ExpenseMainState(
    val expensesDays: List<ExpenseDay> = listOf<ExpenseDay>(),
    val showBottomSheet: Boolean = false,
    val expenseTypeInBottomSheet: ExpenseType? = null,
    val expenseDateInBottomSheet: LocalDate? = null,
    val expenseAmountInBottomSheet: Double? = null,
    val expenseIdInBottomSheet: Long? = null,
    val carIdForExpenseBottomSheet: Long? = null,
    val deleteDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
