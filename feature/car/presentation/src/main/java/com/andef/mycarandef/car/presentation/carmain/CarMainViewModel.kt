package com.andef.mycarandef.car.presentation.carmain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.usecases.GetAllCarsUseCase
import com.andef.mycarandef.car.domain.usecases.RemoveCarUseCase
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
import javax.inject.Inject

class CarMainViewModel @Inject constructor(
    private val getAllCarsUseCase: GetAllCarsUseCase,
    private val removeCarUseCase: RemoveCarUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CarMainState())
    val state: StateFlow<CarMainState> = _state

    fun send(intent: CarMainIntent) {
        when (intent) {
            is CarMainIntent.SaveScrollState -> {
                _state.value = _state.value.copy(
                    initialFirstVisibleItemIndex = intent.initialFirstVisibleItemIndex,
                    initialFirstVisibleItemScrollOffset = intent.initialFirstVisibleItemScrollOffset
                )
            }

            CarMainIntent.GetCars -> getCars()
            is CarMainIntent.BottomSheetVisibleChange -> changeBottomSheetVisible(
                isVisible = intent.isVisible,
                registrationMark = intent.registrationMark,
                imageUriInBottomSheet = intent.imageUriInBottomSheet,
                year = intent.year,
                model = intent.model,
                brand = intent.brand,
                carId = intent.carId
            )

            is CarMainIntent.ChangeDeleteDialogVisible -> changeDeleteDialogVisible(
                isVisible = intent.isVisible
            )

            is CarMainIntent.DeleteCar -> deleteCar(
                carId = intent.carId,
                onError = intent.onError,
                currentCarId = intent.currentCarId
            )

            is CarMainIntent.ChooseCurrentCar -> chooseCurrentCar(
                carId = intent.carId,
                carName = intent.carName,
                carImageUri = intent.carImageUri
            )
        }
    }

    private fun chooseCurrentCar(carId: Long, carName: String, carImageUri: String?) {
        setCurrentCarIdUseCase.invoke(carId)
        setCurrentCarNameUseCase.invoke(carName)
        setCurrentCarImageUriUseCase.invoke(carImageUri)
    }

    private fun changeDeleteDialogVisible(isVisible: Boolean) {
        _state.value = _state.value.copy(deleteDialogVisible = isVisible)
    }

    private fun deleteCar(carId: Long, currentCarId: Long, onError: (String) -> Unit) {
        if (carId == currentCarId) {
            onError("Нельзя удалить выбранный текущим автомобиль!")
            return
        }
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                withContext(Dispatchers.IO) { removeCarUseCase.invoke(carId) }
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun changeBottomSheetVisible(
        isVisible: Boolean,
        registrationMark: String? = null,
        year: Int? = null,
        model: String? = null,
        imageUriInBottomSheet: String? = null,
        brand: String? = null,
        carId: Long? = null
    ) {
        _state.value = _state.value.copy(
            showBottomSheet = isVisible,
            registrationMarkOnBottomSheet = registrationMark,
            brandInBottomSheet = brand,
            imageUriInBottomSheet = imageUriInBottomSheet,
            modelInBottomSheet = model,
            yearInBottomSheet = year,
            carIdInBottomSheet = carId
        )
    }

    private var firstStart: Boolean = true
    private var job: Job? = null
    private fun getCars() {
        if (firstStart == true || state.value.isError) {
            firstStart = false
            job?.cancel()
            job = viewModelScope.launch {
                getAllCarsUseCase.invoke()
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
                                cars = listOf()
                            )
                        }
                    }
                    .collect {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                cars = it,
                                isError = false
                            )
                        }
                    }
            }
        }
    }
}