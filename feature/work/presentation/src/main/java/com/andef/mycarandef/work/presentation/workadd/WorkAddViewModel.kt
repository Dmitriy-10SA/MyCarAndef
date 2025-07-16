package com.andef.mycarandef.work.presentation.workadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.work.domain.entities.Work
import com.andef.mycarandef.work.domain.usecases.AddWorkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class WorkAddViewModel @Inject constructor(
    private val addWorkUseCase: AddWorkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WorkAddState())
    val state: StateFlow<WorkAddState> = _state

    fun send(intent: WorkAddIntent) {
        when (intent) {
            is WorkAddIntent.ChangeWorkTitle -> changeInput(workTitle = intent.workTitle)
            is WorkAddIntent.ChangeMileage -> changeInput(mileage = intent.mileage)
            is WorkAddIntent.ChangeNote -> changeInput(note = intent.note)
            is WorkAddIntent.ChangeDate -> changeInput(date = intent.date)
            is WorkAddIntent.SaveClick -> saveClick(
                onSuccess = intent.onSuccess,
                onError = intent.onError,
                carId = intent.carId,
                workTitle = _state.value.workTitle,
                mileage = _state.value.mileage ?: throw IllegalArgumentException(),
                note = _state.value.note,
                date = _state.value.date ?: throw IllegalArgumentException()
            )

            is WorkAddIntent.ChangeDatePickerVisible -> changeDatePickerVisible(
                isVisible = intent.isVisible
            )
        }
    }

    private fun changeDatePickerVisible(isVisible: Boolean) {
        _state.value = _state.value.copy(datePickerVisible = isVisible)
    }

    private fun saveClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        workTitle: String,
        mileage: Int,
        note: String?,
        date: LocalDate,
        carId: Long,
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) {
                    addWorkUseCase.invoke(
                        Work(
                            id = 0,
                            title = workTitle,
                            note = note,
                            mileage = mileage,
                            date = date,
                            carId = carId
                        )
                    )
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
        workTitle: String = _state.value.workTitle,
        mileage: Int? = _state.value.mileage,
        note: String? = _state.value.note,
        date: LocalDate? = _state.value.date
    ) {
        _state.value = _state.value.copy(
            workTitle = workTitle,
            mileage = mileage,
            note = note,
            date = date,
            saveButtonEnabled = workTitle.isNotEmpty() && mileage != null && date != null
        )
    }
}