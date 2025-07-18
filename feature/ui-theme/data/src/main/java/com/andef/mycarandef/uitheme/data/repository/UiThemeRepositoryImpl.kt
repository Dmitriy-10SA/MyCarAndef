package com.andef.mycarandef.uitheme.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.uitheme.domain.repository.UiThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class UiThemeRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences
) : UiThemeRepository {
    private val isLightThemeAsFlow = MutableStateFlow(false)

    override fun getIsLightThemeAsFlow(): Flow<Boolean> {
        return isLightThemeAsFlow.asStateFlow()
    }

    override fun getIsLightTheme(isSystemInDarkTheme: Boolean): Boolean {
        val isLightTheme = shPrefs.getBoolean(IS_LIGHT_THEME, !isSystemInDarkTheme)
        isLightThemeAsFlow.value = isLightTheme
        return isLightTheme
    }

    override fun setTheme(isLightTheme: Boolean) {
        shPrefs.edit { putBoolean(IS_LIGHT_THEME, isLightTheme) }
        isLightThemeAsFlow.value = isLightTheme
    }

    companion object {
        private const val IS_LIGHT_THEME = "is-light-theme"
    }
}