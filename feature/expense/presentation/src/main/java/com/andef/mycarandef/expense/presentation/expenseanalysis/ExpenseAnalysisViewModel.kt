package com.andef.mycarandef.expense.presentation.expenseanalysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.usecases.GetExpensesByCarIdAsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ExpenseAnalysisViewModel @Inject constructor(
    private val getExpensesByCarIdAsListUseCase: GetExpensesByCarIdAsListUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseAnalysisState())
    val state: StateFlow<ExpenseAnalysisState> = _state

    fun send(intent: ExpenseAnalysisIntent) {
        when (intent) {
            is ExpenseAnalysisIntent.CurrentCarChoose -> {
                currentCarChoose(car = intent.car)
            }

            is ExpenseAnalysisIntent.DatesChange -> {
                datesChange(startDate = intent.startDate, endDate = intent.endDate)
            }

            is ExpenseAnalysisIntent.SelectedTabIdChange -> {
                val lastSelectedDateTabId = _state.value.selectedDateTabId
                _state.value = _state.value.copy(
                    selectedDateTabId = intent.id,
                    lastSelectedDateTabId = lastSelectedDateTabId
                )
            }

            ExpenseAnalysisIntent.ChooseLastSelectedTabId -> {
                _state.value = _state.value.copy(
                    selectedDateTabId = _state.value.lastSelectedDateTabId
                )
            }

            is ExpenseAnalysisIntent.RangePickerVisibleChange -> {
                _state.value = _state.value.copy(dateRangePickerVisible = intent.isVisible)
            }

            is ExpenseAnalysisIntent.LoadExpenses -> loadExpenses(carId = intent.carId)
        }
    }

    private fun datesChange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                startDate = startDate,
                endDate = endDate
            )
            val filterByDateExpenses = filterExpenses(
                expenses = _state.value.expenses,
                startDate = _state.value.startDate,
                endDate = _state.value.endDate
            )
            val totalSumForScreen = getTotalSumForScreen(filterByDateExpenses)
            val expensesInfoForScreen = getExpensesInfoForScreen(
                filterByDateExpenses = filterByDateExpenses,
                totalSumForScreen = totalSumForScreen
            )
            _state.value = _state.value.copy(
                isLoading = false,
                totalSumForScreen = totalSumForScreen,
                expensesInfoForScreen = expensesInfoForScreen
            )
        }
    }

    private fun loadExpenses(carId: Long) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, isError = false)
                val expenses = withContext(Dispatchers.IO) {
                    getExpensesByCarIdAsListUseCase.invoke(carId)
                }
                val filterByDateExpenses = filterExpenses(
                    expenses = expenses,
                    startDate = _state.value.startDate,
                    endDate = _state.value.endDate
                )
                val totalSumForScreen = getTotalSumForScreen(filterByDateExpenses)
                val expensesInfoForScreen = getExpensesInfoForScreen(
                    filterByDateExpenses = filterByDateExpenses,
                    totalSumForScreen = totalSumForScreen
                )
                _state.value = _state.value.copy(
                    expenses = expenses,
                    totalSumForScreen = totalSumForScreen,
                    expensesInfoForScreen = expensesInfoForScreen
                )
            } catch (_: Exception) {
                _state.value = _state.value.copy(isError = true)
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun getTotalSumForScreen(filterByDateExpenses: List<Expense>): Double {
        return withContext(Dispatchers.IO) {
            filterByDateExpenses.sumOf { it.amount }
        }
    }

    private suspend fun filterExpenses(
        expenses: List<Expense>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<Expense> {
        return withContext(Dispatchers.IO) {
            expenses.filter {
                it.date >= startDate && it.date <= endDate
            }
        }
    }

    private suspend fun getExpensesInfoForScreen(
        filterByDateExpenses: List<Expense>,
        totalSumForScreen: Double
    ): Map<ExpenseType, Pair<Float, Double>> {
        return withContext(Dispatchers.IO) {
            mutableMapOf<ExpenseType, Pair<Float, Double>>().apply {
                filterByDateExpenses
                    .groupBy { it.type }
                    .map {
                        val totalAmountForType = it.value.sumOf { it.amount }
                        put(
                            it.key,
                            share(totalAmountForType, totalSumForScreen) to totalAmountForType
                        )
                    }
            }
        }
    }

    private fun share(amount: Double, total: Double): Float =
        if (total > 0.0) ((amount / total) * 100f).toFloat() else 0f

    private fun currentCarChoose(car: Car) {
        setCurrentCarIdUseCase.invoke(car.id)
        setCurrentCarNameUseCase.invoke("${car.brand} ${car.model}")
        setCurrentCarImageUriUseCase.invoke(car.photo)
    }
}