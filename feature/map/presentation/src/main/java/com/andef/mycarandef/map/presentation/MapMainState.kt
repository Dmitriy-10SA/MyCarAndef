package com.andef.mycarandef.map.presentation

import com.andef.mycarandef.car.domain.entities.Car

data class MapMainState(
    val currentCar: Car? = null,
    val finePermission: Boolean = false,
    val coarsePermission: Boolean = false,
    val isErrorSnackbar: Boolean = false,
    val confirmDialogVisible: Boolean = false,
    val bottomSheetVisible: Boolean = false,
    val latInBottomSheet: Double? = null,
    val lonInBottomSheet: Double? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
