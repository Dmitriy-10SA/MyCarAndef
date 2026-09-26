package com.andef.mycarandef.expense.presentation.expenseadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.usecases.AddExpenseUseCase
import com.andef.mycarandef.expense.domain.usecases.ChangeExpenseUseCase
import com.andef.mycarandef.expense.domain.usecases.GetExpenseByIdUseCase
import com.andef.mycarandef.expense.domain.usecases.GetDefaultExpenseTypeUseCase
import com.andef.mycarandef.expense.domain.usecases.SetDefaultExpenseTypeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class ExpenseAddViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val changeExpenseUseCase: ChangeExpenseUseCase,
    private val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    private val getDefaultExpenseTypeUseCase: GetDefaultExpenseTypeUseCase,
    private val setDefaultExpenseTypeUseCase: SetDefaultExpenseTypeUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseAddState())
    val state: StateFlow<ExpenseAddState> = _state

    fun send(intent: ExpenseAddIntent) {
        when (intent) {
            is ExpenseAddIntent.ChangeAmount -> changeInput(amount = intent.amount)
            is ExpenseAddIntent.ChangeDate -> changeInput(date = intent.date)
            is ExpenseAddIntent.ChangeDatePickerVisible -> changeDatePickerVisible(
                isVisible = intent.isVisible
            )

            is ExpenseAddIntent.ChangeNote -> changeInput(note = intent.note)
            is ExpenseAddIntent.ChangeType -> changeInput(type = intent.type)
            is ExpenseAddIntent.InitDefaultType -> initDefaultType(intent.applyToExpense)
            is ExpenseAddIntent.SetDefaultType -> setDefaultType(intent.type)
            is ExpenseAddIntent.InitExpenseByLateExpense -> initExpenseByLateExpense(
                expenseId = intent.expenseId,
                onError = intent.onError
            )

            is ExpenseAddIntent.SaveClick -> saveClick(
                onSuccess = intent.onSuccess,
                onError = intent.onError,
                amount = _state.value.amount,
                type = _state.value.type,
                note = _state.value.note,
                date = _state.value.date,
                carId = intent.carId
            )
        }
    }

    private fun initDefaultType(applyToExpense: Boolean) {
        val defaultType = getDefaultExpenseTypeUseCase.invoke()
        _state.value = _state.value.copy(defaultType = defaultType)
        if (applyToExpense && defaultType != null) changeInput(type = defaultType)
    }

    private fun setDefaultType(type: ExpenseType?) {
        setDefaultExpenseTypeUseCase.invoke(type)
        _state.value = _state.value.copy(defaultType = type)
        if (type != null && _state.value.isAdd) changeInput(type = type)
    }

    private fun initExpenseByLateExpense(expenseId: Long, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val expense = withContext(Dispatchers.IO) {
                    getExpenseByIdUseCase.invoke(expenseId)
                }
                changeInput(
                    amount = expense.amount,
                    type = expense.type,
                    note = expense.note,
                    date = expense.date
                )
                _state.value = _state.value.copy(isAdd = false, expenseId = expenseId)
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun changeDatePickerVisible(isVisible: Boolean) {
        _state.value = _state.value.copy(datePickerVisible = isVisible)
    }

    private fun saveClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        amount: Long?,
        type: ExpenseType?,
        note: String?,
        date: LocalDate?,
        carId: Long
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val isAdd = _state.value.isAdd
                val expenseId = _state.value.expenseId
                withContext(Dispatchers.IO) {
                    if (isAdd) {
                        addExpenseUseCase.invoke(
                            Expense(
                                id = 0,
                                amount = amount ?: throw IllegalArgumentException(),
                                note = note,
                                type = type ?: throw IllegalArgumentException(),
                                date = date ?: throw IllegalArgumentException(),
                                carId = carId
                            )
                        )
                    } else {
                        changeExpenseUseCase.invoke(
                            id = expenseId ?: throw IllegalArgumentException(),
                            amount = amount ?: throw IllegalArgumentException(),
                            note = note,
                            date = date ?: throw IllegalArgumentException(),
                            type = type ?: throw IllegalArgumentException()
                        )
                    }
                }
                onSuccess()
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun changeInput(
        amount: Long? = _state.value.amount,
        type: ExpenseType? = _state.value.type,
        note: String? = _state.value.note,
        date: LocalDate? = _state.value.date
    ) {
        _state.value = _state.value.copy(
            amount = amount,
            type = type,
            note = note,
            date = date,
            saveButtonEnabled = amount != null && amount != 0L && type != null && date != null
        )
    }
}
