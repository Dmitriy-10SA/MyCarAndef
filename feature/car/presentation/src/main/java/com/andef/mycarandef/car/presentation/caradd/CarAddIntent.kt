package com.andef.mycarandef.car.presentation.caradd

sealed class CarAddIntent {
    data class ChangeBrand(val brand: String) : CarAddIntent()
    data class ChangeModel(val model: String) : CarAddIntent()
    data class ChangePhoto(val photo: String?) : CarAddIntent()
    data class ChangeYear(val year: Int?) : CarAddIntent()
    data class ChangeRegistrationMark(val registrationMark: String?) : CarAddIntent()
    data class SaveClick(
        val currentCarId: Long,
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    ) : CarAddIntent()

    data class InitCarByLateCar(val carId: Long, val onError: (String) -> Unit) : CarAddIntent()
}