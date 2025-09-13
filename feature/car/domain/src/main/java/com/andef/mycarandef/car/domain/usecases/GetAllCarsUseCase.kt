package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.car.domain.repository.CarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAllCarsUseCase @Inject constructor(private val repository: CarRepository) {
    operator fun invoke(): Flow<List<Car>> = repository.getAllCars().flowOn(Dispatchers.IO)
}