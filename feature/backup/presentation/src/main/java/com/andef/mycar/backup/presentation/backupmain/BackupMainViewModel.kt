package com.andef.mycar.backup.presentation.backupmain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycar.backup.domain.BackupData
import com.andef.mycar.reminder.domain.usecases.AddReminderUseCase
import com.andef.mycar.reminder.domain.usecases.GetAllRemindersAsListUseCase
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.GetAllCarsAsListUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarNameUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import com.andef.mycarandef.expense.domain.usecases.AddExpenseUseCase
import com.andef.mycarandef.expense.domain.usecases.GetAllExpensesAsListUseCase
import com.andef.mycarandef.start.domain.usecases.GetUsernameUseCase
import com.andef.mycarandef.start.domain.usecases.SetUsernameUseCase
import com.andef.mycarandef.work.domain.usecases.AddWorkUseCase
import com.andef.mycarandef.work.domain.usecases.GetAllWorksAsListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BackupMainViewModel @Inject constructor(
    private val getCurrentCarIdUseCase: GetCurrentCarIdUseCase,
    private val getCurrentCarNameUseCase: GetCurrentCarNameUseCase,
    private val getCurrentCarImageUriUseCase: GetCurrentCarImageUriUseCase,
    private val getAllCarsAsListUseCase: GetAllCarsAsListUseCase,
    private val getAllWorksAsListUseCase: GetAllWorksAsListUseCase,
    private val getAllExpensesAsListUseCase: GetAllExpensesAsListUseCase,
    private val getAllRemindersAsListUseCase: GetAllRemindersAsListUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val addCarUseCase: AddCarUseCase,
    private val addWorkUseCase: AddWorkUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val setUsernameUseCase: SetUsernameUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BackupMainState())
    val state: StateFlow<BackupMainState> = _state

    fun send(intent: BackupMainIntent) {
        when (intent) {
            is BackupMainIntent.RestoreDate -> restoreData(
                data = intent.data,
                onSuccess = intent.onSuccess,
                onError = intent.onError
            )

            is BackupMainIntent.SaveData -> saveData(
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

    private fun saveData(onSuccess: (BackupData) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, isErrorSnackbar = false)
                val allCars = withContext(Dispatchers.IO) {
                    getAllCarsAsListUseCase.invoke().map {
                        Car(
                            id = it.id,
                            brand = it.brand,
                            model = it.model,
                            photo = null,
                            year = it.year,
                            registrationMark = it.registrationMark,
                            coordinatesLat = it.coordinatesLat,
                            coordinatesLon = it.coordinatesLon
                        )
                    }
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
                val currentCarId = getCurrentCarIdUseCase.invoke()
                val currentCarNameUseCase = getCurrentCarNameUseCase.invoke()
                val currentCarImageUriUseCase = getCurrentCarImageUriUseCase.invoke()
                val backupData = BackupData(
                    allCars = allCars,
                    allWorks = allWorks,
                    allExpenses = allExpenses,
                    allReminders = allReminders,
                    username = username,
                    currentCarId = currentCarId,
                    currentCarName = currentCarNameUseCase,
                    currentCarImageUri = currentCarImageUriUseCase
                )
                onSuccess(backupData)
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