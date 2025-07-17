package com.andef.mycarandef.car.presentation.caradd

data class CarAddState(
    val username: String = "",
    val brand: String = "",
    val model: String = "",
    val photo: String? = null,
    val year: Int? = null,
    val registrationMark: String? = null,
    val coordinatesLat: Double? = null,
    val coordinatesLon: Double? = null,
    val carId: Long? = null,
    val isLoading: Boolean = false,
    val saveButtonEnabled: Boolean = false,
    val isAdd: Boolean = true
)
