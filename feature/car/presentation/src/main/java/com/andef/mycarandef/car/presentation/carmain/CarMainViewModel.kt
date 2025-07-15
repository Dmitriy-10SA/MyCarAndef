package com.andef.mycarandef.car.presentation.carmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.usecases.GetAllCarsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class CarMainViewModel @Inject constructor(
    private val getAllCarsUseCase: GetAllCarsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CarMainState())
    val state: StateFlow<CarMainState> = _state

    fun send(intent: CarMainIntent) {
        when (intent) {
            CarMainIntent.GetCars -> getCars()
        }
    }

    private var job: Job? = null
    private fun getCars() {
        job?.cancel()
        job = viewModelScope.launch {
            getAllCarsUseCase.invoke()
                .onStart { _state.value = _state.value.copy(isLoading = true, isError = false) }
                .catch { _state.value = _state.value.copy(isLoading = false, isError = true) }
                .collect { _state.value = _state.value.copy(isLoading = false, cars = it) }
        }
    }
}