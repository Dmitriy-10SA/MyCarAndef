package com.andef.mycarandef.start.presentation.carinput

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.start.domain.usecases.GetUsernameUseCase
import com.andef.mycarandef.start.domain.usecases.SetIsFirstStartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CarInputViewModel @Inject constructor(
    private val getUsernameUseCase: GetUsernameUseCase,
    private val setIsFirstStartUseCase: SetIsFirstStartUseCase,
    private val setCurrentCarIdUseCase: SetCurrentCarIdUseCase,
    private val setCurrentCarNameUseCase: SetCurrentCarNameUseCase,
    private val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase,
    private val addCarUseCase: AddCarUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CarInputState())
    val state: StateFlow<CarInputState> = _state

    fun send(intent: CarInputIntent) {
        when (intent) {
            CarInputIntent.GetUsername -> getUsername()
            is CarInputIntent.ChangeBrand -> changeInput(brand = intent.brand)
            is CarInputIntent.ChangeModel -> changeInput(model = intent.model)
            is CarInputIntent.ChangePhoto -> changeInput(photo = intent.photo)
            is CarInputIntent.ChangeRegistrationMark -> changeInput(
                registrationMark = intent.registrationMark
            )

            is CarInputIntent.ChangeYear -> changeInput(year = intent.year)
            is CarInputIntent.NextClick -> nextClick(
                onSuccess = intent.onSuccess,
                onError = intent.onError,
                brand = _state.value.brand,
                model = _state.value.model,
                photo = _state.value.photo,
                year = _state.value.year,
                registrationMark = _state.value.registrationMark
            )
        }
    }

    private fun getUsername() {
        val username = getUsernameUseCase.invoke()!!
        _state.value = _state.value.copy(username = username)
    }

    private fun nextClick(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val id = withContext(Dispatchers.IO) {
                    addCarUseCase.invoke(
                        Car(
                            id = 0,
                            brand = brand,
                            model = model,
                            photo = photo,
                            year = year,
                            registrationMark = registrationMark,
                            coordinatesLon = null,
                            coordinatesLat = null
                        )
                    )
                }
                setIsFirstStartUseCase.invoke(false)
                setCurrentCarIdUseCase.invoke(id)
                setCurrentCarNameUseCase.invoke("$brand $model")
                setCurrentCarImageUriUseCase.invoke(photo)
                onSuccess(Screen.MainScreens.WorksMainScreen.route)
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
        registrationMark: String? = _state.value.registrationMark
    ) {
        _state.value = _state.value.copy(
            brand = brand,
            model = model,
            photo = photo,
            year = year,
            registrationMark = registrationMark,
            nextButtonEnabled = brand.isNotEmpty() && model.isNotEmpty()
        )
    }
}