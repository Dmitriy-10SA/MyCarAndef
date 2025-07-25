package com.andef.mycar.backup.presentation.backupmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.reminder.domain.usecases.AddReminderUseCase
import com.andef.mycar.reminder.domain.usecases.GetAllRemindersAsListUseCase
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.GetAllCarsAsListUseCase
import com.andef.mycarandef.expense.domain.usecases.AddExpenseUseCase
import com.andef.mycarandef.expense.domain.usecases.GetAllExpensesAsListUseCase
import com.andef.mycarandef.start.domain.usecases.GetUsernameUseCase
import com.andef.mycarandef.start.domain.usecases.SetUsernameUseCase
import com.andef.mycarandef.work.domain.usecases.AddWorkUseCase
import com.andef.mycarandef.work.domain.usecases.GetAllWorksAsListUseCase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupMainViewModel @Inject constructor(
    private val getAllCarsAsListUseCase: GetAllCarsAsListUseCase,
    private val getAllWorksAsListUseCase: GetAllWorksAsListUseCase,
    private val getAllExpensesAsListUseCase: GetAllExpensesAsListUseCase,
    private val getAllRemindersAsListUseCase: GetAllRemindersAsListUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val addCarUseCase: AddCarUseCase,
    private val addWorkUseCase: AddWorkUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val setUsernameUseCase: SetUsernameUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BackupMainState())
    val state: StateFlow<BackupMainState> = _state

    fun send(intent: BackupMainIntent) {
        when (intent) {
            is BackupMainIntent.RestoreDate -> TODO()
            is BackupMainIntent.SaveData -> saveData(
                onSuccess = intent.onSuccess,
                onError = intent.onError
            )
        }
    }

    private fun saveData(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val allCars = withContext(Dispatchers.IO) {
                    getAllCarsAsListUseCase.invoke()
                }
                val allWorks = withContext(Dispatchers.IO) {
                    getAllWorksAsListUseCase.invoke()
                }
                val allExpenses = withContext(Dispatchers.IO) {
                    getAllExpensesAsListUseCase.invoke()
                }
                val allReminders = withContext(Dispatchers.IO) {
                    getAllRemindersAsListUseCase.invoke()
                }
                val username = getUsernameUseCase.invoke() ?: ""
                val backupData = BackupData(
                    allCars = allCars,
                    allWorks = allWorks,
                    allExpenses = allExpenses,
                    allReminders = allReminders,
                    username = username
                )
                onSuccess(Gson().toJson(backupData))
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}