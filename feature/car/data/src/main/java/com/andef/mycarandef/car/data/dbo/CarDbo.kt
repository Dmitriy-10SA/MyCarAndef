package com.andef.mycarandef.car.data.dbo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car")
data class CarDbo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val brand: String,
    val model: String,
    val photo: String?,
    val year: Int?,
    @ColumnInfo(name = "registration_mark")
    val registrationMark: String?,
    @ColumnInfo(name = "latitude")
    val coordinatesLat: Double?,
    @ColumnInfo(name = "longitude")
    val coordinatesLon: Double?
)