package com.andef.mycarandef.start.domain.usecases

import com.andef.mycarandef.start.domain.repository.StartRepository
import javax.inject.Inject

class GetUsernameUseCase @Inject constructor(private val repository: StartRepository) {
    operator fun invoke() = repository.getUsername()
}