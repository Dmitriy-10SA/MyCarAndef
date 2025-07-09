package com.andef.mycarandef.uitheme.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.uitheme.domain.repository.UiThemeRepository
import javax.inject.Inject

class UiThemeRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences
) : UiThemeRepository {
    override fun getIsLightTheme(isSystemInDarkTheme: Boolean): Boolean {
        return shPrefs.getBoolean(IS_LIGHT_THEME, !isSystemInDarkTheme)
    }

    override fun setTheme(isLightTheme: Boolean) {
        shPrefs.edit { putBoolean(IS_LIGHT_THEME, isLightTheme) }
    }

    companion object {
        private const val IS_LIGHT_THEME = "is-light-theme"
    }
}