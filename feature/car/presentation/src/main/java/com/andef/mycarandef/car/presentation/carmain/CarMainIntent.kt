package com.andef.mycarandef.car.presentation.carmain

sealed class CarMainIntent {
    data object GetCars : CarMainIntent()
    data class BottomSheetVisibleChange(
        val isVisible: Boolean,
        val brand: String? = null,
        val model: String? = null,
        val year: Int? = null,
        val imageUriInBottomSheet: String? = null,
        val registrationMark: String? = null,
        val carId: Long? = null
    ) : CarMainIntent()

    data class SaveScrollState(
        val initialFirstVisibleItemIndex: Int,
        val initialFirstVisibleItemScrollOffset: Int
    ) : CarMainIntent()

    data class ChooseCurrentCar(
        val carId: Long,
        val carName: String,
        val carImageUri: String?
    ) : CarMainIntent()

    data class DeleteCar(
        val carId: Long,
        val onError: (String) -> Unit,
        val currentCarId: Long
    ) : CarMainIntent()

    data class ChangeDeleteDialogVisible(val isVisible: Boolean) : CarMainIntent()
}