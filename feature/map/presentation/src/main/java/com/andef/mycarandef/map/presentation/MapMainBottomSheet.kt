package com.andef.mycarandef.map.presentation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMainBottomSheet(
    isLightTheme: Boolean,
    isVisible: Boolean,
    sheetState: SheetState,
    context: Context,
    viewModel: MapMainViewModel,
    onDismissRequest: () -> Unit,
    lat: Double?,
    lon: Double?,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    lat?.let {
        lon?.let {
            UiModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                isLightTheme = isLightTheme,
                isVisible = isVisible
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Выберите приложение:",
                        color = grayColor(isLightTheme),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(0.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppItem(
                            isLightTheme = isLightTheme,
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_yandex_maps_icon),
                            contentDescription = "Иконка яндекс карты",
                            text = "Яндекс Карты",
                            onClick = {
                                onDismissRequest()
                                launchApp(
                                    context = context,
                                    intent = buildYandexMapsIntent(lat = lat, lon = lon),
                                    viewModel = viewModel,
                                    scope = scope,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                        )
                        DGISItem(isLightTheme = isLightTheme) {
                            onDismissRequest()
                            launchApp(
                                context = context,
                                intent = build2GisNavIntent(lat = lat, lon = lon),
                                viewModel = viewModel,
                                scope = scope,
                                snackbarHostState = snackbarHostState
                            )
                        }
                        AppItem(
                            isLightTheme = isLightTheme,
                            icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_google_maps_icon),
                            contentDescription = "Иконка гугл карты",
                            text = "Google Карты",
                            onClick = {
                                onDismissRequest()
                                launchApp(
                                    context = context,
                                    intent = buildGoogleMapsNavIntent(lat = lat, lon = lon),
                                    viewModel = viewModel,
                                    scope = scope,
                                    snackbarHostState = snackbarHostState
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.DGISItem(isLightTheme: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .size(65.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(color = WhiteColor, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                ),
            painter = painterResource(com.andef.mycarandef.design.R.drawable.my_car_2gis_icon),
            contentDescription = "Иконка 2 гис"
        )
        Text(
            text = "2ГИС",
            color = blackOrWhiteColor(isLightTheme),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RowScope.AppItem(
    isLightTheme: Boolean,
    icon: Painter,
    contentDescription: String,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .size(65.dp)
                .background(color = WhiteColor, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(all = 10.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = text,
            color = blackOrWhiteColor(isLightTheme),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

private fun launchApp(
    context: Context,
    intent: Intent,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    viewModel: MapMainViewModel
) {
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        viewModel.send(
            MapMainIntent.ShowErrorSnackbar(
                msg = "Приложение не установлено",
                callback = { msg ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = msg,
                            withDismissAction = true
                        )
                    }
                }
            )
        )
    }
}

private object NavApps {
    const val PKG_YANDEX_MAPS = "ru.yandex.yandexmaps"
    const val PKG_GOOGLE_MAPS = "com.google.android.apps.maps"
    const val PKG_2GIS = "ru.dublgis.dgismobile"
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

private fun build2GisNavIntent(lat: Double, lon: Double): Intent =
    Intent(
        Intent.ACTION_VIEW,
        "dgis://2gis.ru/routeSearch/rsType/pedestrian/to/$lon,$lat".toUri()
    ).apply {
        setPackage(NavApps.PKG_2GIS)
    }