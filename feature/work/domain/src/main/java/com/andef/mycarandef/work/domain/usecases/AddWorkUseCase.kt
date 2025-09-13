package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.entities.Work
import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class AddWorkUseCase @Inject constructor(private val repository: WorkRepository) {
    suspend operator fun invoke(work: Work) = repository.addWork(work)
}