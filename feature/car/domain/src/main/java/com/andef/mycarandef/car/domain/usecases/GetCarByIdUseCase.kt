package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import javax.inject.Inject

class GetCarByIdUseCase @Inject constructor(private val repository: CarRepository) {
    suspend operator fun invoke(id: Long): Car = repository.getCarById(id)
}