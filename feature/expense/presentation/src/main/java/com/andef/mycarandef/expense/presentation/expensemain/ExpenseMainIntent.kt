package com.andef.mycarandef.expense.presentation.expensemain

import android.content.Context
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import java.time.LocalDate

sealed class ExpenseMainIntent {
    data class SubscribeForExpenses(
        val currentCarId: Long,
        val startDate: LocalDate,
        val endDate: LocalDate
    ) : ExpenseMainIntent()

    data class BottomSheetVisibleChange(
        val isVisible: Boolean,
        val expenseDate: LocalDate? = null,
        val expenseType: ExpenseType? = null,
        val expenseAmount: Double? = null,
        val expenseId: Long? = null,
        val carId: Long? = null
    ) : ExpenseMainIntent()

    data class SaveScrollState(
        val initialFirstVisibleItemIndex: Int,
        val initialFirstVisibleItemScrollOffset: Int
    ) : ExpenseMainIntent()

    data class AddToMyFinance(
        val context: Context,
        val amount: Double,
        val date: LocalDate,
        val type: ExpenseType,
        val onSuccess: (String) -> Unit,
        val onAddError: (String) -> Unit,
        val onError: () -> Unit
    ) : ExpenseMainIntent()

    data class DeleteExpense(
        val expenseId: Long,
        val onError: (String) -> Unit
    ) : ExpenseMainIntent()

    data class ChangeDeleteDialogVisible(val isVisible: Boolean) : ExpenseMainIntent()
}