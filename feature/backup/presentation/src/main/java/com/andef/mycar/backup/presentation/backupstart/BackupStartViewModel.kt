package com.andef.mycar.backup.presentation.backupstart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.reminder.domain.usecases.AddReminderUseCase
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import com.andef.mycarandef.expense.domain.usecases.AddExpenseUseCase
import com.andef.mycarandef.start.domain.usecases.SetIsFirstStartUseCase
import com.andef.mycarandef.start.domain.usecases.SetUsernameUseCase
import com.andef.mycarandef.work.domain.usecases.AddWorkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupStartViewModel @Inject constructor(
    private val addCarUseCase: AddCarUseCase,
    private val addWorkUseCase: AddWorkUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val setUsernameUseCase: SetUsernameUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase,
    private val setIsFirstStartUseCase: SetIsFirstStartUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BackupStartState())
    val state: StateFlow<BackupStartState> = _state

    fun send(intent: BackupStartIntent) {
        when (intent) {
            is BackupStartIntent.RestoreDate -> restoreData(
                data = intent.data,
                onSuccess = intent.onSuccess,
                onError = intent.onError
            )
        }
    }

    private fun restoreData(
        data: BackupData,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, isErrorSnackbar = false)
                withContext(Dispatchers.IO) {
                    data.allCars.forEach { addCarUseCase.invoke(it) }
                    data.allWorks.forEach { addWorkUseCase.invoke(it) }
                    data.allExpenses.forEach { addExpenseUseCase.invoke(it) }
                    data.allReminders.forEach { addReminderUseCase.invoke(it) }
                }
                setCurrentCarIdUseCase.invoke(data.currentCarId)
                setCurrentCarNameUseCase.invoke(data.currentCarName)
                setCurrentCarImageUriUseCase.invoke(data.currentCarImageUri)
                setUsernameUseCase.invoke(data.username)
                setIsFirstStartUseCase.invoke(false)
                onSuccess("Данные успешно восстановлены!")
            } catch (e: Exception) {
                Log.e("BackupViewModel", e.toString())
                _state.value = _state.value.copy(isErrorSnackbar = true)
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}