package com.andef.mycarandef.map.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.usecases.ChangeCarUseCase
import com.andef.mycarandef.car.domain.usecases.GetCarByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private class SaveCoordinatesException : Exception()

class MapMainViewModel @Inject constructor(
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val changeCarUseCase: ChangeCarUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MapMainState())
    val state: StateFlow<MapMainState> = _state

    fun send(intent: MapMainIntent) {
        when (intent) {
            is MapMainIntent.LoadCurrentCarAndCheckPermissions -> {
                loadCurrentCar(
                    carId = intent.carId,
                    context = intent.context,
                    onOnlyForCoarse = intent.onOnlyForCoarse
                )
            }

            is MapMainIntent.ShowErrorSnackbar -> {
                _state.value = _state.value.copy(isErrorSnackbar = true)
                intent.callback(intent.msg)
            }

            is MapMainIntent.ChangeBottomSheetVisible -> {
                _state.value = _state.value.copy(
                    bottomSheetVisible = intent.isVisible,
                    latInBottomSheet = intent.lat,
                    lonInBottomSheet = intent.lon
                )
            }

            is MapMainIntent.SaveCarCoordinates -> {
                saveCoordinates(
                    context = intent.context,
                    onError = intent.onError,
                    onSuccess = intent.onSuccess
                )
            }

            is MapMainIntent.BuildRouteToCar -> {
                getCoordinates(latAndLon = intent.latAndLon)
            }

            is MapMainIntent.PermissionChanged -> {
                _state.value = _state.value.copy(
                    finePermission = intent.fine,
                    coarsePermission = intent.coarse
                )
            }

            is MapMainIntent.ConfirmDialogVisibleChange -> {
                _state.value = _state.value.copy(confirmDialogVisible = intent.isVisible)
            }
        }
    }

    private fun getCoordinates(latAndLon: (Double, Double) -> Unit) {
        _state.value.currentCar?.let { car ->
            car.coordinatesLat?.let { lat ->
                car.coordinatesLon?.let { lon ->
                    latAndLon(lat, lon)
                }
            }
        }
    }

    private fun saveCoordinates(
        context: Context,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        _state.value.currentCar?.let { car ->
            viewModelScope.launch {
                try {
                    _state.value = _state.value.copy(isLoading = true, isErrorSnackbar = false)
                    val coordinates = withContext(Dispatchers.IO) {
                        getPreciseLocationOnce(context)
                    }
                    if (coordinates == null) throw SaveCoordinatesException()
                    withContext(Dispatchers.IO) {
                        changeCarUseCase.invoke(
                            id = car.id,
                            brand = car.brand,
                            model = car.model,
                            photo = car.photo,
                            year = car.year,
                            registrationMark = car.registrationMark,
                            coordinatesLon = coordinates.second,
                            coordinatesLat = coordinates.first
                        )
                    }
                    _state.value = _state.value.copy(
                        currentCar = Car(
                            id = car.id,
                            brand = car.brand,
                            model = car.model,
                            photo = car.photo,
                            year = car.year,
                            registrationMark = car.registrationMark,
                            coordinatesLon = coordinates.second,
                            coordinatesLat = coordinates.first
                        )
                    )
                    onSuccess("Местоположение успешно сохранено!")
                } catch (_: SaveCoordinatesException) {
                    _state.value = _state.value.copy(isErrorSnackbar = true)
                    onError("Невозможно определить точное местоположение! Проверьте, включен ли GPS!")
                } catch (_: Exception) {
                    _state.value = _state.value.copy(isErrorSnackbar = true)
                    onError("Ошибка! Попробуйте ещё раз!")
                } finally {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }

    private var lastCurrentCarId: Long? = null
    private var job: Job? = null
    private fun loadCurrentCar(carId: Long, context: Context, onOnlyForCoarse: (String) -> Unit) {
        _state.value = _state.value.copy(
            finePermission = hasLocationFinePermission(context),
            coarsePermission = hasLocationCoarsePermission(context),
            isErrorSnackbar = false
        )
        if ((_state.value.coarsePermission == true) && (_state.value.finePermission == false)) {
            _state.value = _state.value.copy(isErrorSnackbar = true)
            onOnlyForCoarse("Предоставьте доступ к точному местоположению!")
        }
        if (lastCurrentCarId == null || carId != lastCurrentCarId || state.value.isError) {
            lastCurrentCarId = carId
            job?.cancel()
            job = viewModelScope.launch {
                try {
                    _state.value = _state.value.copy(isLoading = true, isError = false)
                    val car = withContext(Dispatchers.IO) { getCarByIdUseCase.invoke(carId) }
                    _state.value = _state.value.copy(currentCar = car)
                } catch (_: Exception) {
                    _state.value = _state.value.copy(isError = true, currentCar = null)
                } finally {
                    _state.value = _state.value.copy(isLoading = false)
                }
            }
        }
    }
}