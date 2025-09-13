package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class RemoveWorkUseCase @Inject constructor(private val repository: WorkRepository) {
    suspend operator fun invoke(id: Long) = repository.removeWork(id)
}