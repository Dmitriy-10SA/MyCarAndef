package com.andef.mycarandef.car.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.car.data.dao.CarDao
import com.andef.mycarandef.car.data.mapper.CarMapper
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences,
    private val carMapper: CarMapper,
    private val carDao: CarDao
) : CarRepository {
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
    }

    override fun setCurrentCarName(name: String) {
        shPrefs.edit { putString(CURRENT_CAR_NAME, name) }
    }

    override fun getCurrentCarName(): String {
        return shPrefs.getString(CURRENT_CAR_NAME, "").toString()
    }

    companion object {
        private const val CURRENT_CAR_NAME = "current-car-name"
        private const val CURRENT_CAR_ID = "current-car-id"
    }
}