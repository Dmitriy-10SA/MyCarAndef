package com.andef.mycarandef.expense.presentation.expensemain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.expense.domain.entities.ExpenseDay
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.usecases.GetExpensesByCarIdUseCase
import com.andef.mycarandef.expense.domain.usecases.RemoveExpenseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ExpenseMainViewModel @Inject constructor(
    private val getExpensesByCarIdUseCase: GetExpensesByCarIdUseCase,
    private val removeExpenseUseCase: RemoveExpenseUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseMainState())
    val state: StateFlow<ExpenseMainState> = _state

    fun send(intent: ExpenseMainIntent) {
        when (intent) {
            is ExpenseMainIntent.SubscribeForExpenses -> subscribeForExpenses(
                currentCarId = intent.currentCarId
            )

            is ExpenseMainIntent.BottomSheetVisibleChange -> changeBottomSheetVisible(
                isVisible = intent.isVisible,
                expenseDate = intent.expenseDate,
                expenseType = intent.expenseType,
                expenseAmount = intent.expenseAmount,
                expenseId = intent.expenseId,
                carId = intent.carId
            )

            is ExpenseMainIntent.ChangeDeleteDialogVisible -> changeDeleteDialogVisible(
                isVisible = intent.isVisible
            )

            is ExpenseMainIntent.DeleteExpense -> deleteExpense(
                expenseId = intent.expenseId,
                onError = intent.onError
            )
        }
    }

    private fun changeDeleteDialogVisible(isVisible: Boolean) {
        _state.value = _state.value.copy(deleteDialogVisible = isVisible)
    }

    private fun deleteExpense(expenseId: Long, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) { removeExpenseUseCase.invoke(expenseId) }
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun changeBottomSheetVisible(
        isVisible: Boolean,
        expenseType: ExpenseType? = null,
        expenseAmount: Double? = null,
        expenseId: Long? = null,
        expenseDate: LocalDate? = null,
        carId: Long? = null
    ) {
        _state.value = _state.value.copy(
            showBottomSheet = isVisible,
            expenseIdInBottomSheet = expenseId,
            expenseDateInBottomSheet = expenseDate,
            expenseTypeInBottomSheet = expenseType,
            expenseAmountInBottomSheet = expenseAmount,
            carIdForExpenseBottomSheet = carId
        )
    }

    private var job: Job? = null
    private fun subscribeForExpenses(currentCarId: Long) {
        job?.cancel()
        job = viewModelScope.launch {
            getExpensesByCarIdUseCase.invoke(currentCarId)
                .onStart { _state.value = _state.value.copy(isLoading = true, isError = false) }
                .catch { _state.value = _state.value.copy(isLoading = false, isError = true) }
                .collect {
                    val expenses = it
                        .groupBy { it.date }
                        .toSortedMap(compareByDescending { it })
                        .map { (date, items) -> ExpenseDay(date, items) }
                    _state.value = _state.value.copy(isLoading = false, expensesDays = expenses)
                }
        }
    }
}