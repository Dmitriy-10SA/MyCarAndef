package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import javax.inject.Inject

class AddCarUseCase @Inject constructor(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.addCar(car)
}