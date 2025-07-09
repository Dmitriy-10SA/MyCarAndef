package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCarsUseCase @Inject constructor(private val repository: CarRepository) {
    operator fun invoke(): Flow<List<Car>> = repository.getAllCars()
}