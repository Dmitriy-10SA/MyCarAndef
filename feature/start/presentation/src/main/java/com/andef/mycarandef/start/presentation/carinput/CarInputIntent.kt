package com.andef.mycarandef.start.presentation.carinput

sealed class CarInputIntent {
    data object GetUsername : CarInputIntent()
    data class ChangeBrand(val brand: String) : CarInputIntent()
    data class ChangeModel(val model: String) : CarInputIntent()
    data class ChangePhoto(val photo: String?) : CarInputIntent()
    data class ChangeYear(val year: Int?) : CarInputIntent()
    data class ChangeRegistrationMark(val registrationMark: String?) : CarInputIntent()
    data class NextClick(
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : CarInputIntent()
}