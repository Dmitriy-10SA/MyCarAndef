package com.andef.mycarandef.map.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andef.mycar.core.ads.InterstitialAdManager
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.RedColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapMainScreen(
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long,
    interstitialAdManager: InterstitialAdManager
) {
    val viewModel: MapMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val coarse = hasLocationCoarsePermission(context)
        val fine = hasLocationFinePermission(context)
        if ((coarse == true) && (fine == true)) {
            viewModel.send(MapMainIntent.PermissionChanged(fine = true, coarse = true))
        } else if (coarse == true) {
            viewModel.send(MapMainIntent.PermissionChanged(fine = false, coarse = true))
            viewModel.send(
                MapMainIntent.LoadCurrentCarAndCheckPermissions(
                    carId = currentCarId,
                    context = context,
                    onOnlyForCoarse = { msg ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            )
        } else {
            viewModel.send(MapMainIntent.PermissionChanged(fine = false, coarse = false))
        }
    }

    LaunchedEffect(
        currentCarId,
        hasLocationFinePermission(context) && hasLocationCoarsePermission(context)
    ) {
        viewModel.send(
            MapMainIntent.LoadCurrentCarAndCheckPermissions(
                currentCarId,
                context,
                onOnlyForCoarse = { msg ->
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = msg,
                            withDismissAction = true,
                            duration = SnackbarDuration.Long
                        )
                    }
                }
            )
        )
    }

    OnResume {
        val coarse = hasLocationCoarsePermission(context)
        val fine = hasLocationFinePermission(context)
        if ((coarse == true) && (fine == true)) {
            viewModel.send(MapMainIntent.PermissionChanged(fine = true, coarse = true))
        } else if (coarse == true) {
            viewModel.send(MapMainIntent.PermissionChanged(fine = false, coarse = true))
        } else {
            viewModel.send(MapMainIntent.PermissionChanged(fine = false, coarse = false))
        }
    }

    MainContent(
        viewModel = viewModel,
        state = state,
        currentCarId = currentCarId,
        isLightTheme = isLightTheme,
        context = context,
        paddingValues = paddingValues,
        scope = scope,
        snackbarHostState = snackbarHostState,
        settingLauncher = settingsLauncher,
        interstitialAdManager = interstitialAdManager
    )
    UiSnackbar(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        type = if (state.value.isError || state.value.isErrorSnackbar) {
            UiSnackbarType.Error
        } else {
            UiSnackbarType.Success
        }
    )
    ConfirmDialog(
        isLightTheme = isLightTheme,
        viewModel = viewModel,
        state = state,
        scope = scope,
        context = context,
        snackbarHostState = snackbarHostState,
        interstitialAdManager = interstitialAdManager
    )
    MapMainBottomSheet(
        sheetState = sheetState,
        context = context,
        onDismissRequest = {
            viewModel.send(
                MapMainIntent.ChangeBottomSheetVisible(
                    isVisible = false,
                    lat = null,
                    lon = null
                )
            )
        },
        isLightTheme = isLightTheme,
        isVisible = state.value.bottomSheetVisible,
        lat = state.value.latInBottomSheet,
        lon = state.value.lonInBottomSheet,
        viewModel = viewModel,
        scope = scope,
        snackbarHostState = snackbarHostState,
        interstitialAdManager = interstitialAdManager
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    viewModel: MapMainViewModel,
    context: Context,
    scope: CoroutineScope,
    state: State<MapMainState>,
    snackbarHostState: SnackbarHostState,
    settingLauncher: ManagedActivityResultLauncher<Intent, androidx.activity.result.ActivityResult>,
    currentCarId: Long,
    isLightTheme: Boolean,
    paddingValues: PaddingValues,
    interstitialAdManager: InterstitialAdManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(0.dp))
        Map(isLightTheme = isLightTheme, state = state)
        if (!state.value.finePermission) {
            UiButton(
                text = "Разрешить доступ к местоположению",
                onClick = {
                    settingLauncher.launch(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            UiButton(
                text = "Сохранить новое местоположение",
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.send(MapMainIntent.ConfirmDialogVisibleChange(isVisible = true))
                },
                modifier = Modifier.fillMaxWidth()
            )
            UiButton(
                text = "Построить маршрут до автомобиля",
                onClick = {
                    viewModel.send(
                        MapMainIntent.BuildRouteToCar(
                            latAndLon = { lat, lon ->
                                viewModel.send(
                                    MapMainIntent.ChangeBottomSheetVisible(
                                        isVisible = true,
                                        lat = lat,
                                        lon = lon
                                    )
                                )
                            }
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.currentCar?.coordinatesLat != null &&
                        state.value.currentCar?.coordinatesLon != null
            )
        }
        Spacer(modifier = Modifier.padding(0.dp))
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = {
            viewModel.send(
                MapMainIntent.LoadCurrentCarAndCheckPermissions(
                    currentCarId,
                    context,
                    onOnlyForCoarse = { msg ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                        }
                    }
                )
            )
        }
    )
}

@Composable
private fun ConfirmDialog(
    isLightTheme: Boolean,
    viewModel: MapMainViewModel,
    context: Context,
    state: State<MapMainState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    interstitialAdManager: InterstitialAdManager
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Сохранение нового местоположения",
        subtitle = "Вы уверены? Старое местоположение будет удалено!",
        yesTitle = "Сохранить",
        yesTitleColor = GreenColor,
        cancelTitle = "Отмена",
        cancelTitleColor = RedColor,
        onDismissRequest = {
            viewModel.send(MapMainIntent.ConfirmDialogVisibleChange(isVisible = false))
        },
        onYesClick = {
            interstitialAdManager.showAd {
                viewModel.send(MapMainIntent.ConfirmDialogVisibleChange(isVisible = false))
                viewModel.send(
                    MapMainIntent.SaveCarCoordinates(
                        onSuccess = { msg ->
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = msg,
                                    withDismissAction = true
                                )
                            }
                        },
                        context = context,
                        onError = { msg ->
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = msg,
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long
                                )
                            }
                        }
                    )
                )
            }
        },
        onCancelClick = {
            viewModel.send(MapMainIntent.ConfirmDialogVisibleChange(isVisible = false))
        },
        isVisible = state.value.confirmDialogVisible
    )
}

@Suppress("MissingPermission")
suspend fun getPreciseLocationOnce(context: Context): Pair<Double, Double>? {
    val hasFine = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
    if (!hasFine) return null
    val fused = LocationServices.getFusedLocationProviderClient(context)
    val cts = CancellationTokenSource()
    val current = suspendCancellableCoroutine<Pair<Double, Double>?> { cont ->
        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { loc ->
                if (!cont.isCompleted) {
                    cont.resume(loc?.let { it.latitude to it.longitude }) { cause, _, _ -> null }
                }
            }
            .addOnFailureListener {
                if (!cont.isCompleted) cont.resume(null) { cause, _, _ -> null }
            }
        cont.invokeOnCancellation { cts.cancel() }
    }
    return current
}

fun hasLocationFinePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

fun hasLocationCoarsePermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

@Composable
private fun ColumnScope.Map(isLightTheme: Boolean, state: State<MapMainState>) {
    var lastCarId by remember { mutableStateOf<Long?>(null) }
    var lastCoords by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = grayColor(isLightTheme),
                shape = RoundedCornerShape(16.dp)
            )
            .alpha(if (state.value.finePermission) 1f else 0.3f),
        factory = { context ->
            Configuration.getInstance().load(
                context,
                context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
            )
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(16.5)
                controller.setCenter(
                    org.osmdroid.util.GeoPoint(
                        state.value.currentCar?.coordinatesLat ?: 55.751244,
                        state.value.currentCar?.coordinatesLon ?: 37.618423
                    )
                )
            }
        },
        update = { map ->
            val car = state.value.currentCar
            val currentCarId = car?.id
            val newLat = car?.coordinatesLat
            val newLon = car?.coordinatesLon

            val existingMarker = map.overlays
                .filterIsInstance<Marker>()
                .firstOrNull { it.id == "car_marker" }

            if (newLat != null && newLon != null) {
                val newPoint = org.osmdroid.util.GeoPoint(newLat, newLon)

                if (existingMarker == null) {
                    map.overlays.add(
                        Marker(map).apply {
                            id = "car_marker"
                            position = newPoint
                            infoWindow = null
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            icon = ContextCompat.getDrawable(
                                map.context,
                                com.andef.mycarandef.design.R.drawable.my_car_car_location
                            )
                        }
                    )
                } else {
                    existingMarker.position = newPoint
                }

                val coordsChanged = lastCoords?.let { (prevLat, prevLon) ->
                    val dLat = kotlin.math.abs(prevLat - newLat)
                    val dLon = kotlin.math.abs(prevLon - newLon)
                    dLat > 0.00005 || dLon > 0.00005
                } != false

                val carChanged = currentCarId != null && currentCarId != lastCarId

                if (coordsChanged || carChanged) {
                    map.controller.animateTo(newPoint)
                }

                lastCoords = newLat to newLon
            } else {
                if (existingMarker != null) {
                    map.overlays.remove(existingMarker)
                }
                lastCoords = null
            }

            if (currentCarId != lastCarId) {
                lastCarId = currentCarId
            }

            map.invalidate()
        }
    )
}

@Composable
private fun OnResume(action: () -> Unit) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                action()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}