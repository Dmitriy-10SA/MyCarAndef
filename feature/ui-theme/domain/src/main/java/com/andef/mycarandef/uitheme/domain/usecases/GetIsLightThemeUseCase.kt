package com.andef.mycarandef.uitheme.domain.usecases

import com.andef.mycarandef.uitheme.domain.repository.UiThemeRepository
import javax.inject.Inject

class GetIsLightThemeUseCase @Inject constructor(private val repository: UiThemeRepository) {
    operator fun invoke(isSystemInDarkTheme: Boolean) =
        repository.getIsLightTheme(isSystemInDarkTheme)
}