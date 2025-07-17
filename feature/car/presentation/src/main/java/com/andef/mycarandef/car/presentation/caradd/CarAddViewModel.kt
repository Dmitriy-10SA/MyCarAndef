package com.andef.mycarandef.car.presentation.caradd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.ChangeCarUseCase
import com.andef.mycarandef.car.domain.usecases.GetCarByIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarAddViewModel @Inject constructor(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val addCarUseCase: AddCarUseCase,
    private val changeCarUseCase: ChangeCarUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CarAddState())
    val state: StateFlow<CarAddState> = _state

    fun send(intent: CarAddIntent) {
        when (intent) {
            is CarAddIntent.ChangeBrand -> changeInput(brand = intent.brand)
            is CarAddIntent.ChangeModel -> changeInput(model = intent.model)
            is CarAddIntent.ChangePhoto -> changeInput(photo = intent.photo)
            is CarAddIntent.ChangeRegistrationMark -> changeInput(
                registrationMark = intent.registrationMark
            )

            is CarAddIntent.ChangeYear -> changeInput(year = intent.year)
            is CarAddIntent.SaveClick -> saveClick(
                onSuccess = intent.onSuccess,
                onError = intent.onError,
                brand = _state.value.brand,
                model = _state.value.model,
                photo = _state.value.photo,
                year = _state.value.year,
                registrationMark = _state.value.registrationMark,
                coordinatesLat = _state.value.coordinatesLat,
                coordinatesLon = _state.value.coordinatesLon,
                currentCarId = intent.currentCarId
            )

            is CarAddIntent.InitCarByLateCar -> initCarByLateCar(
                carId = intent.carId,
                onError = intent.onError
            )
        }
    }

    private fun initCarByLateCar(carId: Long, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val car = withContext(Dispatchers.IO) { getCarByIdUseCase.invoke(carId) }
                changeInput(
                    brand = car.brand,
                    model = car.model,
                    photo = car.photo,
                    year = car.year,
                    registrationMark = car.registrationMark,
                    coordinatesLat = car.coordinatesLat,
                    coordinatesLon = car.coordinatesLon
                )
                _state.value = _state.value.copy(isAdd = false, carId = carId)
            } catch (_: Exception) {
                onError("Ошибка! Попробуйте ещё раз!")
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private fun saveClick(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        currentCarId: Long,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?,
        coordinatesLat: Double?,
        coordinatesLon: Double?
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val isAdd = _state.value.isAdd
                val carId = _state.value.carId
                withContext(Dispatchers.IO) {
                    if (isAdd) {
                        addCarUseCase.invoke(
                            Car(
                                id = 0,
                                brand = brand,
                                model = model,
                                photo = photo,
                                year = year,
                                registrationMark = registrationMark,
                                coordinatesLat = null,
                                coordinatesLon = null
                            )
                        )
                    } else {
                        changeCarUseCase.invoke(
                            id = carId ?: throw IllegalArgumentException(),
                            brand = brand,
                            model = model,
                            photo = photo,
                            year = year,
                            registrationMark = registrationMark,
                            coordinatesLat = coordinatesLat,
                            coordinatesLon = coordinatesLon
                        )
                        if (carId == currentCarId) {
                            setCurrentCarNameUseCase.invoke("$brand $model")
                            setCurrentCarImageUriUseCase.invoke(photo)
                        }
                    }
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
        brand: String = _state.value.brand,
        model: String = _state.value.model,
        photo: String? = _state.value.photo,
        year: Int? = _state.value.year,
        registrationMark: String? = _state.value.registrationMark,
        coordinatesLat: Double? = _state.value.coordinatesLat,
        coordinatesLon: Double? = _state.value.coordinatesLon,
    ) {
        _state.value = _state.value.copy(
            brand = brand,
            model = model,
            photo = photo,
            year = year,
            registrationMark = registrationMark,
            coordinatesLat = coordinatesLat,
            coordinatesLon = coordinatesLon,
            saveButtonEnabled = brand.isNotEmpty() && model.isNotEmpty()
        )
    }
}