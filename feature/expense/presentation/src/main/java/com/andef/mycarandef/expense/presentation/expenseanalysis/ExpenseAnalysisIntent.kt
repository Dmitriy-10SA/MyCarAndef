package com.andef.mycarandef.expense.presentation.expenseanalysis

import com.andef.mycarandef.car.domain.entities.Car
import java.time.LocalDate

sealed class ExpenseAnalysisIntent {
    data class LoadExpenses(val carId: Long) : ExpenseAnalysisIntent()
    data class CurrentCarChoose(val car: Car) : ExpenseAnalysisIntent()
    data class SelectedTabIdChange(val id: Int) : ExpenseAnalysisIntent()
    data class RangePickerVisibleChange(val isVisible: Boolean) : ExpenseAnalysisIntent()
    data class DatesChange(
        val startDate: LocalDate,
        val endDate: LocalDate
    ) : ExpenseAnalysisIntent()
    data object ChooseLastSelectedTabId : ExpenseAnalysisIntent()
}