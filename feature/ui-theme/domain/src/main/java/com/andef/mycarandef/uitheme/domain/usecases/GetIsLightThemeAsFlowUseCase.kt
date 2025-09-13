package com.andef.mycarandef.uitheme.domain.usecases

import com.andef.mycarandef.uitheme.domain.repository.UiThemeRepository
import javax.inject.Inject

class GetIsLightThemeAsFlowUseCase @Inject constructor(private val repository: UiThemeRepository) {
    operator fun invoke() = repository.getIsLightThemeAsFlow()
}