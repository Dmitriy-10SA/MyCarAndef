package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class GetWorkByIdUseCase @Inject constructor(private val repository: WorkRepository) {
    suspend operator fun invoke(id: Long) = repository.getWorkById(id)
}