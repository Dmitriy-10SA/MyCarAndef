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

    override suspend fun getCarById(id: Int): Car {
        return carMapper.map(carDao.getCarById(id))
    }

    override suspend fun changeCar(
        id: Int,
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

    override suspend fun removeCar(id: Int) {
        carDao.removeCar(id)
    }

    override suspend fun addCar(car: Car) {
        carDao.addCar(carMapper.map(car))
    }

    override fun getFavoriteCarId(): Int {
        val id = shPrefs.getInt(FAVORITE_CAR_ID, -1)
        return when (id) {
            -1 -> throw IllegalArgumentException("Обязательно должна быть выбрана любимая машина!")
            else -> id
        }
    }

    override fun setFavoriteCarId(id: Int) {
        shPrefs.edit { putInt(FAVORITE_CAR_ID, id) }
    }

    companion object {
        private const val FAVORITE_CAR_ID = "favorite-car-id"
    }
}