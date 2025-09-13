package com.andef.mycarandef.start.domain.usecases

import com.andef.mycarandef.start.domain.repository.StartRepository
import javax.inject.Inject

class SetIsFirstStartUseCase @Inject constructor(private val repository: StartRepository) {
    operator fun invoke(flag: Boolean) = repository.setIsFirstStart(flag)
}