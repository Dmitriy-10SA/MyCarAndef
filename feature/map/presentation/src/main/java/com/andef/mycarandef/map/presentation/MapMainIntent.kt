package com.andef.mycarandef.map.presentation

import android.content.Context

sealed class MapMainIntent {
    data class LoadCurrentCarAndCheckPermissions(
        val carId: Long,
        val context: Context,
        val onOnlyForCoarse: (String) -> Unit
    ) : MapMainIntent()

    data class SaveCarCoordinates(
        val context: Context,
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : MapMainIntent()

    data class PermissionChanged(val fine: Boolean, val coarse: Boolean) : MapMainIntent()
    data class ConfirmDialogVisibleChange(val isVisible: Boolean) : MapMainIntent()
    data class ChangeBottomSheetVisible(
        val isVisible: Boolean,
        val lat: Double?,
        val lon: Double?
    ) : MapMainIntent()

    data class ShowErrorSnackbar(val msg: String, val callback: (String) -> Unit) : MapMainIntent()

    data class BuildRouteToCar(val latAndLon: (Double, Double) -> Unit) : MapMainIntent()
}