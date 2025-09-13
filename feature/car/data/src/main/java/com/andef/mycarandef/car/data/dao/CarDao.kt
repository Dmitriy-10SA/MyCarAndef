package com.andef.mycarandef.car.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycarandef.car.data.dbo.CarDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM car ORDER BY LOWER(brand || ' ' || model) ASC")
    suspend fun getAllCarsAsList(): List<CarDbo>

    @Query("SELECT * FROM car ORDER BY LOWER(brand || ' ' || model) ASC")
    fun getAllCars(): Flow<List<CarDbo>>

    @Query("SELECT * FROM car WHERE id = :id")
    suspend fun getCarById(id: Long): CarDbo

    @Query(
        """
        UPDATE car
        SET brand = :brand, model = :model, photo = :photo, year = :year, 
        registration_mark = :registrationMark, latitude = :coordinatesLat, 
        longitude = :coordinatesLon
        WHERE id = :id
        """
    )
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

    @Query("DELETE FROM car WHERE id = :id")
    suspend fun removeCar(id: Long)

    @Insert(onConflict = REPLACE)
    suspend fun addCar(carDbo: CarDbo): Long
}