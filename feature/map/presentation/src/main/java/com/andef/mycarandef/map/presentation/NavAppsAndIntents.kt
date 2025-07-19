package com.andef.mycarandef.map.presentation

import android.content.Intent
import androidx.core.net.toUri

private object NavApps {
    const val PKG_YANDEX_NAVIGATOR = "ru.yandex.yandexnavi"
    const val PKG_YANDEX_MAPS = "ru.yandex.yandexmaps"
    const val PKG_2GIS = "ru.dublgis.dgismobile"
    const val PKG_GOOGLE_MAPS = "com.google.android.apps.maps"
}

fun buildYandexNavigatorIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "yandexnavi://build_route_on_map?lat_to=$lat&lon_to=$lon".toUri()
    ).apply {
        setPackage(NavApps.PKG_YANDEX_NAVIGATOR)
    }

fun buildYandexMapsIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "yandexmaps://maps.yandex.ru/?pt=$lon,$lat&z=16&l=map".toUri()
    ).apply {
        setPackage(NavApps.PKG_YANDEX_MAPS)
    }

fun build2GisIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        // См. доки 2ГИС; формат routeSearch/to/<lon>,<lat>
        "dgis://2gis.ru/routeSearch/to/$lon,$lat".toUri()
    ).apply {
        setPackage(NavApps.PKG_2GIS)
    }

fun buildGoogleMapsNavIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "google.navigation:q=$lat,$lon".toUri()
    ).apply {
        setPackage(NavApps.PKG_GOOGLE_MAPS)
    }