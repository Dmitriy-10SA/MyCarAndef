package com.andef.mycarandef.start.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.andef.mycarandef.start.domain.repository.StartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class StartRepositoryImpl @Inject constructor(
    private val shPrefs: SharedPreferences
) : StartRepository {
    private val usernameAsFlow = MutableStateFlow(getUsername())

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
        usernameAsFlow.value = username
    }

    override fun getUsernameAsFlow(): Flow<String?> = usernameAsFlow.asStateFlow()

    companion object {
        private const val IS_FIRST_START = "is-first-start"
        private const val USERNAME = "username"
    }
}