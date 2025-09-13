package com.andef.mycarandef.car.presentation.carmain

import android.app.Application
import android.content.Context
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.car.ui.UiCarCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.BlackColor
import com.andef.mycarandef.design.theme.DarkGrayColor
import com.andef.mycarandef.design.theme.GrayForDarkColor
import com.andef.mycarandef.design.theme.GrayForLightColor
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.RedColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.YellowColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.yandex.mobile.ads.nativeads.NativeAd
import com.yandex.mobile.ads.nativeads.template.NativeBannerView
import com.yandex.mobile.ads.nativeads.template.SizeConstraint
import com.yandex.mobile.ads.nativeads.template.appearance.BannerAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.ButtonAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.ImageAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.NativeTemplateAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.RatingAppearance
import com.yandex.mobile.ads.nativeads.template.appearance.TextAppearance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    val viewModel: CarMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val application = LocalContext.current.applicationContext as Application
    val adsViewModel: CarMainAdsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CarMainAdsViewModel(application) as T
            }
        }
    )
    val adViews = adsViewModel.adViews.collectAsState().value

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.value.initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = state.value.initialFirstVisibleItemScrollOffset
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.send(
                CarMainIntent.SaveScrollState(
                    initialFirstVisibleItemIndex = listState.firstVisibleItemIndex,
                    initialFirstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
                )
            )
        }
    }

    LaunchedEffect(Unit) { viewModel.send(CarMainIntent.GetCars) }

    MainContent(
        paddingValues = paddingValues,
        state = state,
        viewModel = viewModel,
        isLightTheme = isLightTheme,
        context = context,
        listState = listState,
        adViews = adViews
    )
    BottomSheetWithDeleteDialog(
        navHostController = navHostController,
        viewModel = viewModel,
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        state = state,
        scope = scope,
        snackbarHostState = snackbarHostState,
        currentCarId = currentCarId
    )
    UiSnackbar(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        type = UiSnackbarType.Error
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetWithDeleteDialog(
    navHostController: NavHostController,
    viewModel: CarMainViewModel,
    sheetState: SheetState,
    isLightTheme: Boolean,
    state: State<CarMainState>,
    scope: CoroutineScope,
    currentCarId: Long,
    snackbarHostState: SnackbarHostState
) {
    state.value.carIdInBottomSheet?.let { carId ->
        state.value.brandInBottomSheet?.let { brand ->
            state.value.modelInBottomSheet?.let { model ->
                UiModalBottomSheet(
                    onDismissRequest = {
                        viewModel.send(
                            CarMainIntent.BottomSheetVisibleChange(isVisible = false)
                        )
                    },
                    sheetState = sheetState,
                    isLightTheme = isLightTheme,
                    isVisible = state.value.showBottomSheet
                ) {
                    BottomSheetContent(
                        isLightTheme = isLightTheme,
                        brand = brand,
                        showCurrentAction = currentCarId != carId,
                        model = model,
                        onDeleteClick = {
                            viewModel.send(
                                CarMainIntent.ChangeDeleteDialogVisible(isVisible = true)
                            )
                        },
                        year = state.value.yearInBottomSheet,
                        registrationMark = state.value.registrationMarkOnBottomSheet,
                        onChooseCurrentClick = {
                            viewModel.send(
                                CarMainIntent.BottomSheetVisibleChange(isVisible = false)
                            )
                            viewModel.send(
                                CarMainIntent.ChooseCurrentCar(
                                    carId = carId,
                                    carName = "$brand $model",
                                    carImageUri = state.value.imageUriInBottomSheet
                                )
                            )
                        },
                        onEditClick = {
                            viewModel.send(
                                CarMainIntent.BottomSheetVisibleChange(isVisible = false)
                            )
                            navHostController.navigate(
                                Screen.CarScreen.passId(id = carId)
                            )
                        }
                    )
                }
                DeleteDialog(
                    isLightTheme = isLightTheme,
                    carId = carId,
                    viewModel = viewModel,
                    scope = scope,
                    state = state,
                    snackbarHostState = snackbarHostState,
                    currentCarId = currentCarId
                )
            }
        }
    }
}

@Composable
private fun DeleteDialog(
    carId: Long,
    isLightTheme: Boolean,
    viewModel: CarMainViewModel,
    state: State<CarMainState>,
    scope: CoroutineScope,
    currentCarId: Long,
    snackbarHostState: SnackbarHostState
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Удаление авто",
        subtitle = "Вы уверены? Данное действие невозможно отменить!",
        yesTitle = "Удалить",
        yesTitleColor = RedColor,
        cancelTitle = "Отмена",
        cancelTitleColor = GreenColor,
        onDismissRequest = {
            viewModel.send(
                CarMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        onYesClick = {
            viewModel.send(
                CarMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
            viewModel.send(
                CarMainIntent.BottomSheetVisibleChange(isVisible = false)
            )
            viewModel.send(
                CarMainIntent.DeleteCar(
                    carId = carId,
                    currentCarId = currentCarId,
                    onError = { msg ->
                        snackbarHostState.currentSnackbarData?.dismiss()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true
                            )
                        }
                    }
                )
            )
        },
        onCancelClick = {
            viewModel.send(
                CarMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        isVisible = state.value.deleteDialogVisible
    )
}

@Composable
private fun BottomSheetContent(
    isLightTheme: Boolean,
    brand: String,
    model: String,
    registrationMark: String?,
    year: Int?,
    showCurrentAction: Boolean,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onChooseCurrentClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Column {
            Text(
                text = "$brand $model",
                fontSize = 16.sp,
                color = blackOrWhiteColor(isLightTheme)
            )
            val text = buildString {
                if (registrationMark.isNullOrBlank()) {
                    append("Номер не указан")
                } else {
                    append(registrationMark)
                }
                year?.let {
                    append(" (${it}г)")
                } ?: append(" (год не указан)")
            }.toString()
            Text(
                text = text,
                fontSize = 14.sp,
                color = grayColor(isLightTheme)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (showCurrentAction) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onChooseCurrentClick)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.my_car_star),
                    tint = GreenColor,
                    contentDescription = "Звезда (сделать текущим)"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Выбрать текущим",
                    color = GreenColor,
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onEditClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.my_car_edit),
                tint = blackOrWhiteColor(isLightTheme),
                contentDescription = "Карандаш (изменить)"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Изменить", color = blackOrWhiteColor(isLightTheme), fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onDeleteClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.my_car_delete),
                tint = RedColor,
                contentDescription = "Корзина"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Удалить", color = RedColor, fontSize = 16.sp)
        }
    }
}

@Composable
private fun MainContent(
    paddingValues: PaddingValues,
    state: State<CarMainState>,
    viewModel: CarMainViewModel,
    isLightTheme: Boolean,
    context: Context,
    listState: LazyListState,
    adViews: List<NativeAd>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(0.dp)) }
        items(items = state.value.cars, key = { it.id }) { car ->
            UiCarCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .animateItem(),
                onClick = {
                    viewModel.send(
                        CarMainIntent.BottomSheetVisibleChange(
                            isVisible = true,
                            brand = car.brand,
                            model = car.model,
                            year = car.year,
                            imageUriInBottomSheet = car.photo,
                            registrationMark = car.registrationMark,
                            carId = car.id
                        )
                    )
                },
                isLightTheme = isLightTheme,
                car = car,
                context = context
            )
        }
        item { Spacer(modifier = Modifier.height(0.dp)) }
        item {
            Text(
                text = "Возможно, Вас заинтересует:",
                color = blackOrWhiteColor(isLightTheme),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp)
                    .animateItem(tween(810, easing = FastOutSlowInEasing)),
                textAlign = TextAlign.Center
            )
        }
        items(adViews.size, key = { "$isLightTheme-$it" }) { index ->
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .animateItem(tween(810, easing = FastOutSlowInEasing)),
                factory = { context ->
                    NativeBannerView(context).apply {
                        applyAppearance(nativeAdAppearance(isLightTheme))
                        setAd(adViews[index])
                    }
                }
            )
        }
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = { viewModel.send(CarMainIntent.GetCars) }
    )
}

fun nativeAdAppearance(isLightTheme: Boolean): NativeTemplateAppearance {
    val backgroundColor = if (isLightTheme) WhiteColor else DarkGrayColor
    val titleColor = if (isLightTheme) BlackColor else WhiteColor
    val bodyColor = if (isLightTheme) GrayForLightColor else GrayForDarkColor
    val ageColor = if (isLightTheme) GrayForLightColor else GrayForDarkColor
    val ratingStarColor = YellowColor

    return NativeTemplateAppearance.Builder()
        .withBannerAppearance(
            BannerAppearance.Builder()
                .setBackgroundColor(backgroundColor.toArgb())
                .setBorderWidth(0.1f)
                .setBorderColor(bodyColor.copy(alpha = 0.2f).toArgb())
                .build()
        )
        .withImageAppearance(
            ImageAppearance.Builder()
                .setWidthConstraint(SizeConstraint(SizeConstraint.SizeConstraintType.FIXED, 60f))
                .build()
        )
        .withCallToActionAppearance(
            ButtonAppearance.Builder()
                .setNormalColor(GreenColor.toArgb())
                .setPressedColor(GreenColor.toArgb())
                .setTextAppearance(
                    TextAppearance.Builder()
                        .setTextColor(WhiteColor.toArgb())
                        .setTextSize(14f)
                        .build()
                )
                .build()
        )
        .withDomainAppearance(
            TextAppearance.Builder()
                .setTextColor(bodyColor.toArgb())
                .setTextSize(12f)
                .build()
        )
        .withAgeAppearance(
            TextAppearance.Builder()
                .setTextColor(ageColor.toArgb())
                .setTextSize(12f)
                .build()
        )
        .withBodyAppearance(
            TextAppearance.Builder()
                .setTextColor(bodyColor.toArgb())
                .setTextSize(12f)
                .build()
        )
        .withRatingAppearance(
            RatingAppearance.Builder()
                .setProgressStarColor(ratingStarColor.toArgb())
                .build()
        )
        .withTitleAppearance(
            TextAppearance.Builder()
                .setTextColor(titleColor.toArgb())
                .setTextSize(14f)
                .build()
        )
        .withReviewCountAppearance(
            TextAppearance.Builder()
                .setTextColor(bodyColor.toArgb())
                .setTextSize(12f)
                .build()
        )
        .withSponsoredAppearance(
            TextAppearance.Builder()
                .setTextColor(bodyColor.toArgb())
                .setTextSize(10f)
                .build()
        )
        .withWarningAppearance(
            TextAppearance.Builder()
                .setTextColor(bodyColor.toArgb())
                .setTextSize(10f)
                .build()
        )
        .build()
}