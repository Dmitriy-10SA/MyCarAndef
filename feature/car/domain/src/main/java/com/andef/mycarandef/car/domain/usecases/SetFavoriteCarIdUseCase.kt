package com.andef.mycarandef.car.domain.usecases

import com.andef.mycarandef.car.domain.repository.CarRepository
import javax.inject.Inject

class SetFavoriteCarIdUseCase @Inject constructor(private val repository: CarRepository) {
    operator fun invoke(id: Int) = repository.setFavoriteCarId(id)
}