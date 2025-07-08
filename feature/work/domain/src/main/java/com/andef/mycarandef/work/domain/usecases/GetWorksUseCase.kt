package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class GetWorksUseCase @Inject constructor(private val repository: WorkRepository) {
    operator fun invoke() = repository.getWorks()
}