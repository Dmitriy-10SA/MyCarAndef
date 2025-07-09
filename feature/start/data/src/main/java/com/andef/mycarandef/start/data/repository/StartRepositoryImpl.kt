package com.andef.mycarandef.start.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.start.domain.repository.StartRepository
import javax.inject.Inject

class StartRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences
) : StartRepository {
    override fun getIsFirstStart(): Boolean {
        return shPrefs.getBoolean(IS_FIRST_START, true)
    }

    override fun setIsFirstStart(flag: Boolean) {
        shPrefs.edit { putBoolean(IS_FIRST_START, flag) }
    }

    override fun getUsername(): String? {
        return shPrefs.getString(USERNAME, null)
    }

    override fun setUsername(username: String) {
        shPrefs.edit { putString(USERNAME, username) }
    }

    companion object {
        private const val IS_FIRST_START = "is-first-start"
        private const val USERNAME = "username"
    }
}