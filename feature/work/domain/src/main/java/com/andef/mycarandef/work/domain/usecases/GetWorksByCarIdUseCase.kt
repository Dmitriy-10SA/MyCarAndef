package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class GetWorksByCarIdUseCase @Inject constructor(private val repository: WorkRepository) {
    operator fun invoke(carId: Long) = repository.getWorksByCarId(carId)
}