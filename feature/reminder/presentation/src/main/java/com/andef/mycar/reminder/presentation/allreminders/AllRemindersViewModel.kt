package com.andef.mycar.reminder.presentation.allreminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycar.reminder.domain.usecases.GetRemindersByCarIdUseCase
import com.andef.mycar.reminder.domain.usecases.RemoveReminderUseCase
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
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

class AllRemindersViewModel @Inject constructor(
    private val removeReminderUseCase: RemoveReminderUseCase,
    private val getRemindersByCarIdUseCase: GetRemindersByCarIdUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AllRemindersState())
    val state: StateFlow<AllRemindersState> = _state

    fun send(intent: AllRemindersIntent) {
        when (intent) {
            is AllRemindersIntent.CurrentCarChoose -> currentCarChoose(car = intent.car)
            is AllRemindersIntent.DateSelected -> dateSelected(intent.date)
            is AllRemindersIntent.SubscribeToReminders -> subscribeToReminders(carId = intent.carId)
            is AllRemindersIntent.ReminderBottomSheetVisibleChange -> {
                _state.value = _state.value.copy(
                    reminderSheetVisible = intent.isVisible,
                    reminderIdInBottomSheet = intent.reminderId,
                    reminderTextInBottomSheet = intent.reminderText,
                    reminderDateInBottomSheet = intent.reminderDate,
                    reminderTimeInBottomSheet = intent.reminderTime
                )
            }

            is AllRemindersIntent.DeleteReminder -> deleteReminder(
                id = intent.id,
                onError = intent.onError
            )

            is AllRemindersIntent.DeleteDialogVisibleChange -> {
                _state.value = _state.value.copy(deleteDialogVisible = intent.isVisible)
            }
        }
    }

    private fun deleteReminder(id: Long, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) { removeReminderUseCase.invoke(id) }
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun dateSelected(date: LocalDate) {
        viewModelScope.launch {
            val reminders = _state.value.reminders
            _state.value = _state.value.copy(currentDate = date, isLoading = true)
            val remindersForScreenAsList = withContext(Dispatchers.IO) {
                reminders.filter { it.date == _state.value.currentDate }
            }
            _state.value = _state.value.copy(
                remindersForScreenAsList = remindersForScreenAsList,
                isLoading = false
            )
        }
    }

    private var lastCurrentCarId: Long? = null
    private var job: Job? = null
    private fun subscribeToReminders(carId: Long) {
        if (lastCurrentCarId == null || carId != lastCurrentCarId || state.value.isError) {
            lastCurrentCarId = carId
            job?.cancel()
            job = viewModelScope.launch {
                val today = LocalDate.now()
                val previousMonday = today.minusWeeks(1).with(java.time.DayOfWeek.MONDAY)
                val endSunday = today.plusWeeks(3).with(java.time.DayOfWeek.SUNDAY)
                getRemindersByCarIdUseCase.invoke(carId, previousMonday, endSunday)
                    .onStart {
                        _state.value = _state.value.copy(isLoading = true, isError = false)
                    }
                    .catch {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isError = true,
                            reminders = listOf(),
                            remindersForScreenAsList = listOf(),
                            remindersLocalDatesForScreenAsSet = setOf()
                        )
                    }
                    .collect { reminders ->
                        val remindersForScreenAsList = withContext(Dispatchers.IO) {
                            reminders.filter { it.date == _state.value.currentDate }
                        }
                        val remindersLocalDatesForScreenAsSet = withContext(Dispatchers.IO) {
                            reminders.map { it.date }.toSet()
                        }
                        _state.value = _state.value.copy(
                            isLoading = false,
                            remindersForScreenAsList = remindersForScreenAsList,
                            remindersLocalDatesForScreenAsSet = remindersLocalDatesForScreenAsSet,
                            reminders = reminders,
                            isError = false
                        )
                    }
            }
        }
    }

    private fun currentCarChoose(car: Car) {
        setCurrentCarIdUseCase.invoke(car.id)
        setCurrentCarNameUseCase.invoke("${car.brand} ${car.model}")
        setCurrentCarImageUriUseCase.invoke(car.photo)
    }
}