package com.andef.mycarandef.car.presentation.carmain

sealed class CarMainIntent {
    data object GetCars : CarMainIntent()
}