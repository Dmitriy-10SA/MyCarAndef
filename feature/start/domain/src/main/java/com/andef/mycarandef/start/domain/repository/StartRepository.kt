package com.andef.mycarandef.start.domain.repository

interface StartRepository {
    fun getIsFirstStart(): Boolean
    fun setIsFirstStart(flag: Boolean)
    fun getUsername(): String?
    fun setUsername(username: String)
}