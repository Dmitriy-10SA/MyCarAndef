package com.andef.mycarandef.map.presentation

import android.content.Context

sealed class MapMainIntent {
    data class LoadCurrentCarAndCheckPermissions(
        val carId: Long,
        val context: Context,
        val onOnlyForCoarse: (String) -> Unit
    ) : MapMainIntent()
    data class SaveCarCoordinates(
        val lat: Double,
        val lon: Double,
        val onSuccess: (String) -> Unit,
        val onError: (String) -> Unit
    ) : MapMainIntent()
    data class PermissionChanged(val fine: Boolean, val coarse: Boolean) : MapMainIntent()

    data class BuildRouteToCar(val latAndLon: (Double, Double) -> Unit) : MapMainIntent()
}