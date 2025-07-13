package com.andef.mycarandef.start.presentation.usernameinput

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.start.domain.usecases.SetUsernameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UsernameInputViewModel @Inject constructor(
    private val setUsernameUseCase: SetUsernameUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UsernameInputState())
    val state: StateFlow<UsernameInputState> = _state

    fun send(intent: UsernameInputIntent) {
        when (intent) {
            is UsernameInputIntent.UsernameChange -> changeInput(username = intent.username)
            is UsernameInputIntent.NextClick -> nextClick(
                onSuccess = intent.onSuccess,
                onError = intent.onError
            )
        }
    }

    private fun nextClick(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            setUsernameUseCase.invoke(state.value.username)
            onSuccess(Screen.StartScreens.CarInputScreen.route)
        } catch (_: Exception) {
            onError("Ошибка! Попробуйте ещё раз!")
        }
    }

    private fun changeInput(username: String = _state.value.username) {
        _state.value = _state.value.copy(
            username = username,
            nextButtonEnabled = username.length >= 2
        )
    }
}