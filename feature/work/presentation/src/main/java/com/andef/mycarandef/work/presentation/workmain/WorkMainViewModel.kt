package com.andef.mycarandef.work.presentation.workmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.work.domain.usecases.GetWorksByCarIdUseCase
import com.andef.mycarandef.work.domain.usecases.RemoveWorkUseCase
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

class WorkMainViewModel @Inject constructor(
    private val getWorksByCarIdUseCase: GetWorksByCarIdUseCase,
    private val removeWorkUseCase: RemoveWorkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WorkMainState())
    val state: StateFlow<WorkMainState> = _state

    fun send(intent: WorkMainIntent) {
        when (intent) {
            is WorkMainIntent.SaveScrollState -> {
                _state.value = _state.value.copy(
                    initialFirstVisibleItemIndex = intent.initialFirstVisibleItemIndex,
                    initialFirstVisibleItemScrollOffset = intent.initialFirstVisibleItemScrollOffset
                )
            }

            is WorkMainIntent.SubscribeForWorks -> subscribeForWorks(
                currentCarId = intent.currentCarId
            )

            is WorkMainIntent.BottomSheetVisibleChange -> changeBottomSheetVisible(
                isVisible = intent.isVisible,
                workMileage = intent.workMileage,
                workTitle = intent.workTitle,
                workDate = intent.workDate,
                workId = intent.workId,
                carId = intent.carId
            )

            is WorkMainIntent.DeleteWork -> deleteWork(
                workId = intent.workId,
                onError = intent.onError
            )

            is WorkMainIntent.ChangeDeleteDialogVisible -> changeDeleteDialogVisible(
                isVisible = intent.isVisible
            )
        }
    }

    private fun changeDeleteDialogVisible(isVisible: Boolean) {
        _state.value = _state.value.copy(deleteDialogVisible = isVisible)
    }

    private fun deleteWork(workId: Long, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) { removeWorkUseCase.invoke(workId) }
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun changeBottomSheetVisible(
        isVisible: Boolean,
        workTitle: String? = null,
        workDate: LocalDate? = null,
        workMileage: Int? = null,
        workId: Long? = null,
        carId: Long? = null
    ) {
        _state.value = _state.value.copy(
            showBottomSheet = isVisible,
            workIdInBottomSheet = workId,
            workTitleInBottomSheet = workTitle,
            mileageInBottomSheet = workMileage,
            workDateInBottomSheet = workDate,
            carIdForWorkBottomSheet = carId
        )
    }

    private var lastCurrentCarId: Long? = null
    private var job: Job? = null
    private fun subscribeForWorks(currentCarId: Long) {
        if (lastCurrentCarId == null || currentCarId != lastCurrentCarId || state.value.isError) {
            lastCurrentCarId = currentCarId
            job?.cancel()
            job = viewModelScope.launch {
                getWorksByCarIdUseCase.invoke(currentCarId)
                    .onStart {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(isLoading = true, isError = false)
                        }
                    }
                    .catch {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                isError = true,
                                works = listOf()
                            )
                        }
                    }
                    .collect {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                works = it,
                                isError = false
                            )
                        }
                    }
            }
        }
    }
}