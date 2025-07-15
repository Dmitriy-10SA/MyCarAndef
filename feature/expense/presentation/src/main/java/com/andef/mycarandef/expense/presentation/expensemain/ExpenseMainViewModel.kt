package com.andef.mycarandef.expense.presentation.expensemain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.expense.domain.usecases.GetExpensesByCarIdUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExpenseMainViewModel @Inject constructor(
    private val getExpensesByCarIdUseCase: GetExpensesByCarIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseMainState())
    val state: StateFlow<ExpenseMainState> = _state

    fun send(intent: ExpenseMainIntent) {
        when (intent) {
            is ExpenseMainIntent.SubscribeForExpenses -> subscribeForExpenses(
                currentCarId = intent.currentCarId
            )
        }
    }

    private var job: Job? = null
    private fun subscribeForExpenses(currentCarId: Long) {
        job?.cancel()
        job = viewModelScope.launch {
            getExpensesByCarIdUseCase.invoke(currentCarId)
                .onStart { _state.value = _state.value.copy(isLoading = true, isError = false) }
                .catch { _state.value = _state.value.copy(isLoading = false, isError = true) }
                .collect { _state.value = _state.value.copy(isLoading = false, expenses = it) }
        }
    }
}