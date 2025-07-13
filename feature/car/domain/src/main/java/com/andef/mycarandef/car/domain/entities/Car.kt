package com.andef.mycarandef.car.domain.entities

data class Car(
    val id: Long,
    val brand: String,
    val model: String,
    val photo: String?,
    val year: Int?,
    val registrationMark: String?,
    val coordinatesLat: Double?,
    val coordinatesLon: Double?
)