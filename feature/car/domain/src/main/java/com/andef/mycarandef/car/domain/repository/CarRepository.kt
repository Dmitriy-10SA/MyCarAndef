package com.andef.mycarandef.car.domain.repository

import com.andef.mycarandef.car.domain.entities.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    suspend fun getAllCarsAsList(): List<Car>
    fun getAllCars(): Flow<List<Car>>
    suspend fun getCarById(id: Long): Car
    suspend fun changeCar(
        id: Long,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?,
        coordinatesLat: Double?,
        coordinatesLon: Double?
    )

    suspend fun removeCar(id: Long)
    suspend fun addCar(car: Car): Long
    fun getCurrentCarId(): Long
    fun setCurrentCarId(id: Long)
    fun setCurrentCarName(name: String)
    fun getCurrentCarName(): String
    fun getCurrentCarImageUri(): String?
    fun setCurrentCarImageUri(uri: String?)
    fun getCurrentCarNameAsFlow(): Flow<String>
    fun getCurrentCarIdAsFlow(): Flow<Long>
    fun getCurrentCarImageUriAsFlow(): Flow<String?>
}