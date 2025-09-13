package com.andef.mycarandef.start.domain.repository

import kotlinx.coroutines.flow.Flow

interface StartRepository {
    fun getIsFirstStart(): Boolean
    fun setIsFirstStart(flag: Boolean)
    fun getUsername(): String?
    fun setUsername(username: String)
    fun getUsernameAsFlow(): Flow<String?>
}