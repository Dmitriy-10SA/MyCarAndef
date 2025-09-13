package com.andef.mycarandef.work.domain.usecases

import com.andef.mycarandef.work.domain.repository.WorkRepository
import javax.inject.Inject

class GetAllWorksAsListUseCase @Inject constructor(private val repository: WorkRepository) {
    suspend operator fun invoke() = repository.getAllWorksAsList()
}