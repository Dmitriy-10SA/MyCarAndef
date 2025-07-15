package com.andef.mycarandef.work.presentation.workmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.work.domain.usecases.GetWorksByCarIdUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class WorkMainViewModel @Inject constructor(
    private val getWorksByCarIdUseCase: GetWorksByCarIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WorkMainState())
    val state: StateFlow<WorkMainState> = _state

    fun send(intent: WorkMainIntent) {
        when (intent) {
            is WorkMainIntent.SubscribeForWorks -> subscribeForWorks(
                currentCarId = intent.currentCarId
            )

            is WorkMainIntent.BottomSheetVisibleChange -> changeBottomSheetVisible(
                isVisible = intent.isVisible,
                workTitle = intent.workTitle,
                workDate = intent.workDate,
                workId = intent.workId
            )
        }
    }

    private fun changeBottomSheetVisible(
        isVisible: Boolean,
        workTitle: String? = null,
        workDate: LocalDate? = null,
        workId: Long? = null
    ) {
        _state.value = _state.value.copy(
            showBottomSheet = isVisible,
            workIdInBottomSheet = workId,
            workTitleInBottomSheet = workTitle,
            workDateInBottomSheet = workDate
        )
    }

    private var job: Job? = null
    private fun subscribeForWorks(currentCarId: Long) {
        job?.cancel()
        job = viewModelScope.launch {
            getWorksByCarIdUseCase.invoke(currentCarId)
                .onStart { _state.value = _state.value.copy(isLoading = true, isError = false) }
                .catch { _state.value = _state.value.copy(isLoading = false, isError = true) }
                .collect { _state.value = _state.value.copy(isLoading = false, works = it) }
        }
    }
}