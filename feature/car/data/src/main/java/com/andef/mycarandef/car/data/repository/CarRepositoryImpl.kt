package com.andef.mycarandef.car.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.car.data.dao.CarDao
import com.andef.mycarandef.car.data.mapper.CarMapper
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences,
    private val carMapper: CarMapper,
    private val carDao: CarDao
) : CarRepository {
    private val currentCarIdAsFlow = MutableStateFlow(getCurrentCarId())
    private val currentCarNameAsFlow = MutableStateFlow(getCurrentCarName())
    private val currentCarImageUriAsFlow = MutableStateFlow(getCurrentCarImageUri())

    override fun getCurrentCarIdAsFlow(): Flow<Long> = currentCarIdAsFlow.asStateFlow()

    override fun getCurrentCarImageUriAsFlow(): Flow<String?> = currentCarImageUriAsFlow.asStateFlow()

    override fun getCurrentCarNameAsFlow(): Flow<String> = currentCarNameAsFlow.asStateFlow()

    override suspend fun getAllCarsAsList(): List<Car> {
        return carDao.getAllCarsAsList().map { carDbo -> carMapper.map(carDbo) }
    }

    override fun getAllCars(): Flow<List<Car>> {
        return carDao.getAllCars().map { carsDbo ->
            carsDbo.map { carDbo ->
                carMapper.map(carDbo)
            }
        }
    }

    override suspend fun getCarById(id: Long): Car {
        return carMapper.map(carDao.getCarById(id))
    }

    override suspend fun changeCar(
        id: Long,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?,
        coordinatesLat: Double?,
        coordinatesLon: Double?
    ) {
        carDao.changeCar(
            id = id,
            brand = brand,
            model = model,
            photo = photo,
            year = year,
            registrationMark = registrationMark,
            coordinatesLat = coordinatesLat,
            coordinatesLon = coordinatesLon
        )
    }

    override suspend fun removeCar(id: Long) {
        carDao.removeCar(id)
    }

    override suspend fun addCar(car: Car): Long {
        return carDao.addCar(carMapper.map(car))
    }

    override fun getCurrentCarId(): Long {
        return shPrefs.getLong(CURRENT_CAR_ID, -1)
    }

    override fun setCurrentCarId(id: Long) {
        shPrefs.edit { putLong(CURRENT_CAR_ID, id) }
        currentCarIdAsFlow.value = id
    }

    override fun setCurrentCarName(name: String) {
        shPrefs.edit { putString(CURRENT_CAR_NAME, name) }
        currentCarNameAsFlow.value = name
    }

    override fun getCurrentCarName(): String {
        return shPrefs.getString(CURRENT_CAR_NAME, "").toString()
    }

    override fun getCurrentCarImageUri(): String? {
        return shPrefs.getString(CURRENT_CAR_IMAGE_URI, null)
    }

    override fun setCurrentCarImageUri(uri: String?) {
        shPrefs.edit { putString(CURRENT_CAR_IMAGE_URI, uri) }
        currentCarImageUriAsFlow.value = uri
    }

    companion object {
        private const val CURRENT_CAR_IMAGE_URI = "current-car-image-uri"
        private const val CURRENT_CAR_NAME = "current-car-name"
        private const val CURRENT_CAR_ID = "current-car-id"
    }
}