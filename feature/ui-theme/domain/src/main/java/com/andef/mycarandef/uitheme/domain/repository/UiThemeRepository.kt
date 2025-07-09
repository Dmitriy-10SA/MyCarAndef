package com.andef.mycarandef.uitheme.domain.repository

interface UiThemeRepository {
    fun getIsLightTheme(isSystemInDarkTheme: Boolean): Boolean
    fun setTheme(isLightTheme: Boolean)
}