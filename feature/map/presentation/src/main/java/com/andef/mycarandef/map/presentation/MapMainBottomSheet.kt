package com.andef.mycarandef.map.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMainBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    lat: Double?,
    lon: Double?
) {

}

private fun launchApp(context: Context, intent: Intent) {
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Приложение не установлено", Toast.LENGTH_SHORT).show()
    }
}

private object NavApps {
    const val PKG_YANDEX_MAPS = "ru.yandex.yandexmaps"
    const val PKG_GOOGLE_MAPS = "com.google.android.apps.maps"
}

private fun buildYandexMapsIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "yandexmaps://maps.yandex.ru/?pt=$lon,$lat&z=16&l=map".toUri()
    ).apply {
        setPackage(NavApps.PKG_YANDEX_MAPS)
    }

private fun buildGoogleMapsNavIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "google.navigation:q=$lat,$lon&mode=w".toUri()
    ).apply {
        setPackage(NavApps.PKG_GOOGLE_MAPS)
    }