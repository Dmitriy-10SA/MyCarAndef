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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    val viewModel: MapMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
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
        settingLauncher = settingsLauncher
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
}

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
    paddingValues: PaddingValues
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
                    TODO("Открытие диалога да или нет. После сохранение координат, если да.")
                },
                modifier = Modifier.fillMaxWidth()
            )
            UiButton(
                text = "Построить маршрут до автомобиля",
                onClick = {
                    TODO("Строим маршрут.")
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.currentCar?.coordinatesLat != null &&
                        state.value.currentCar?.coordinatesLon != null
            )
        }
        Spacer(modifier = Modifier.padding(0.dp))
    }
    UiLoading(
        isVisible = state.value.isLoading,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme
    )
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
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (isLightTheme) GrayForLight else GrayForDark,
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
                state.value.currentCar?.let { car ->
                    car.coordinatesLat?.let { lat ->
                        car.coordinatesLon?.let { lon ->
                            val marker = Marker(this)
                            marker.position = org.osmdroid.util.GeoPoint(lat, lon)
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.title = "Авто"
                            overlays.add(marker)
                        }
                    }
                }
            }
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