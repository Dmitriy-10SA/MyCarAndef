package com.andef.mycarandef.car.domain.repository

import com.andef.mycarandef.car.domain.entities.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    fun getAllCars(): Flow<List<Car>>
    suspend fun getCarById(id: Int): Car
    suspend fun changeCar(
        id: Int,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?,
        coordinatesLat: Double?,
        coordinatesLon: Double?
    )

    suspend fun removeCar(id: Int)
    suspend fun addCar(car: Car)
}