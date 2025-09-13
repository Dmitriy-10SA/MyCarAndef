package com.andef.mycarandef.expense.presentation.expensemain

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.expense.domain.entities.ExpenseForLazyColumn
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.expense.domain.usecases.GetExpensesByCarIdUseCase
import com.andef.mycarandef.expense.domain.usecases.RemoveExpenseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
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
            is ExpenseMainIntent.SaveScrollState -> {
                _state.value = _state.value.copy(
                    initialFirstVisibleItemIndex = intent.initialFirstVisibleItemIndex,
                    initialFirstVisibleItemScrollOffset = intent.initialFirstVisibleItemScrollOffset
                )
            }

            is ExpenseMainIntent.SubscribeForExpenses -> subscribeForExpenses(
                currentCarId = intent.currentCarId,
                startDate = intent.startDate,
                endDate = intent.endDate
            )

            is ExpenseMainIntent.AddToMyFinance -> addToMyFinance(
                context = intent.context,
                amount = intent.amount,
                date = intent.date,
                type = intent.type,
                onSuccess = intent.onSuccess,
                onAddError = intent.onAddError,
                onError = intent.onError
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

    private fun addToMyFinance(
        context: Context,
        amount: Double,
        date: LocalDate,
        type: ExpenseType,
        onSuccess: (String) -> Unit,
        onAddError: (String) -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val uri = MY_FINANCE_URI.toUri()
            val note = when (type) {
                ExpenseType.FUEL -> "Заправка автомобиля"
                ExpenseType.WORKS -> "Ремонт автомобиля"
                ExpenseType.WASHING -> "Мойка автомобиля"
                ExpenseType.OTHER -> "Автомобиль - другое"
            }
            val values = ContentValues().apply {
                put(AMOUNT, amount)
                put(DATE, date.toString())
                put(NOTE, note)
            }
            try {
                val resultUri = withContext(Dispatchers.IO) {
                    context.contentResolver.insert(uri, values)
                }
                if (resultUri != null) {
                    _state.value = _state.value.copy(isErrorSnackbar = false)
                    onSuccess("Успешно добавлено в Mои финансы!")
                } else {
                    _state.value = _state.value.copy(isErrorSnackbar = true)
                    onAddError("Ошибка! Попробуйте ещё раз!")
                }
            } catch (e: Exception) {
                Log.d("ASAS", e.toString())
                onError()
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
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
                _state.value = _state.value.copy(isErrorSnackbar = true)
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

    private var lastCurrentCarId: Long? = null
    private var lastStartDate: LocalDate? = null
    private var lastEndDate: LocalDate? = null
    private var job: Job? = null
    private fun subscribeForExpenses(currentCarId: Long, startDate: LocalDate, endDate: LocalDate) {
        if (lastCurrentCarId == null || currentCarId != lastCurrentCarId || state.value.isError || lastStartDate != startDate || lastEndDate != endDate) {
            lastCurrentCarId = currentCarId
            lastStartDate = startDate
            lastEndDate = endDate
            job?.cancel()
            job = viewModelScope.launch {
                getExpensesByCarIdUseCase.invoke(currentCarId, startDate, endDate)
                    .onStart {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(isLoading = true, isError = false)
                        }
                    }
                    .map { expensesList ->
                        val expenses = expensesList
                            .groupBy { expense -> expense.date }
                            .toList()
                            .sortedByDescending { it.first }
                            .map { (date, items) ->
                                val totalAmount = items.sumOf { it.amount }
                                ExpenseForLazyColumn(date, totalAmount, items)
                            }
                        expenses to expensesList.sumOf { it.amount }
                    }
                    .catch {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isError = true,
                                expenses = listOf()
                            )
                        }
                    }
                    .collect { mapPair ->
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                expenses = mapPair.first,
                                totalExpenses = mapPair.second,
                                isError = false
                            )
                        }
                    }
            }
        }
    }

    companion object {
        private const val MY_FINANCE_URI = "content://com.andef.myfinance.expenseprovider/expenses"

        private const val AMOUNT = "amount"
        private const val DATE = "date"
        private const val NOTE = "note"
    }
}