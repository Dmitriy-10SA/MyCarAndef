package com.andef.mycarandef.start.presentation.usernameinput

sealed class UsernameInputIntent {
    data class UsernameChange(val username: String) : UsernameInputIntent()
    data class NextClick(
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : UsernameInputIntent()
}