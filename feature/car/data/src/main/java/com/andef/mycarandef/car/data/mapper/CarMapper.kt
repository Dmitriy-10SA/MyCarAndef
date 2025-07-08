package com.andef.mycarandef.car.data.mapper

import com.andef.mycarandef.car.data.dbo.CarDbo
import com.andef.mycarandef.car.domain.entities.Car
import javax.inject.Inject

class CarMapper @Inject constructor() {
    fun map(car: Car): CarDbo = CarDbo(
        id = car.id,
        brand = car.brand,
        model = car.model,
        photo = car.photo,
        year = car.year,
        registrationMark = car.registrationMark,
        coordinatesLat = car.coordinatesLat,
        coordinatesLon = car.coordinatesLon
    )

    fun map(carDbo: CarDbo): Car = Car(
        id = carDbo.id,
        brand = carDbo.brand,
        model = carDbo.model,
        photo = carDbo.photo,
        year = carDbo.year,
        registrationMark = carDbo.registrationMark,
        coordinatesLat = carDbo.coordinatesLat,
        coordinatesLon = carDbo.coordinatesLon
    )
}