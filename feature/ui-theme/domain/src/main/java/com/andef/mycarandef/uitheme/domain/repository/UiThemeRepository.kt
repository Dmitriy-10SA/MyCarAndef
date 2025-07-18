package com.andef.mycarandef.uitheme.domain.repository

import kotlinx.coroutines.flow.Flow

interface UiThemeRepository {
    fun getIsLightThemeAsFlow(): Flow<Boolean>
    fun getIsLightTheme(isSystemInDarkTheme: Boolean): Boolean
    fun setTheme(isLightTheme: Boolean)
}