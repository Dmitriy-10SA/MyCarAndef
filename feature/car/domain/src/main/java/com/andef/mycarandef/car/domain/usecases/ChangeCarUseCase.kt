package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.repository.CarRepository
import javax.inject.Inject

class ChangeCarUseCase @Inject constructor(private val repository: CarRepository) {
    suspend operator fun invoke(
        id: Long,
        brand: String,
        model: String,
        photo: String?,
        year: Int?,
        registrationMark: String?,
        coordinatesLat: Double?,
        coordinatesLon: Double?
    ) = repository.changeCar(
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