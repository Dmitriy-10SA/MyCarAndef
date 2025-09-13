package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.repository.CarRepository
import javax.inject.Inject

class RemoveCarUseCase @Inject constructor(private val repository: CarRepository) {
    suspend operator fun invoke(id: Long) = repository.removeCar(id)
}