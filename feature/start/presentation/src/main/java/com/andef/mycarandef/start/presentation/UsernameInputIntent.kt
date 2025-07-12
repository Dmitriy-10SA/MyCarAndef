package com.andef.mycarandef.start.presentation

sealed class UsernameInputIntent {
    data class UsernameChange(val username: String) : UsernameInputIntent()
    data class NextClick(val onSuccess: (String) -> Unit) : UsernameInputIntent()
}