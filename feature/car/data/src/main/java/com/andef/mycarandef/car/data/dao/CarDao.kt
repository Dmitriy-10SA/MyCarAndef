package com.andef.mycarandef.car.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.andef.mycarandef.car.data.dbo.CarDbo
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM car")
    fun getAllCars(): Flow<List<CarDbo>>

    @Query("SELECT * FROM car WHERE id = :id")
    suspend fun getCarById(id: Int): CarDbo

    @Query(
        """
        UPDATE car
        SET brand = :brand AND model = :model AND photo = :photo AND year = :year
            AND registration_mark = :registrationMark AND latitude = :coordinatesLat
            AND longitude = :coordinatesLon
        WHERE id = :id
        """
    )
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

    @Query("DELETE FROM car WHERE id = :id")
    suspend fun removeCar(id: Int)

    @Insert(onConflict = REPLACE)
    suspend fun addCar(carDbo: CarDbo)
}