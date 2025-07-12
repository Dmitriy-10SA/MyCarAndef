package com.andef.mycarandef.start.presentation.carinput

data class CarInputState(
    val username: String = "",
    val brand: String = "",
    val model: String = "",
    val photo: String? = null,
    val year: Int? = null,
    val registrationMark: String? = null,
    val nextButtonEnabled: Boolean = false,
    val isLoading: Boolean = false
)
