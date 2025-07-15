package com.andef.mycarandef.car.presentation.carmain

import com.andef.mycarandef.car.domain.entities.Car

data class CarMainState(
    val cars: List<Car> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
