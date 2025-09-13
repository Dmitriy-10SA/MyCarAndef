package com.andef.mycarandef.expense.presentation.expensemain

import com.andef.mycarandef.expense.domain.entities.ExpenseForLazyColumn
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

data class ExpenseMainState(
    val totalExpenses: Double = 0.0,
    val expenses: List<ExpenseForLazyColumn> = emptyList(),
    val showBottomSheet: Boolean = false,
    val expenseTypeInBottomSheet: ExpenseType? = null,
    val expenseDateInBottomSheet: LocalDate? = null,
    val expenseAmountInBottomSheet: Double? = null,
    val expenseIdInBottomSheet: Long? = null,
    val carIdForExpenseBottomSheet: Long? = null,
    val deleteDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isErrorSnackbar: Boolean = true,
    val initialFirstVisibleItemIndex: Int = 0,
    val initialFirstVisibleItemScrollOffset: Int = 0
)
