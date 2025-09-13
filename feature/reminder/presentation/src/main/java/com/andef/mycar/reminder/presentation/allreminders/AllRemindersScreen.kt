package com.andef.mycar.reminder.presentation.allreminders

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.card.car.ui.UiCarInBottomSheetCard
import com.andef.mycarandef.design.card.reminder.ui.UiReminderCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.fab.ui.UiFAB
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
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
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatLocalTimeToString
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllRemindersScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarName: State<String>,
    allCars: List<Car>,
    currentCarImageUri: State<String?>,
    carId: Long
) {
    val viewModel: AllRemindersViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val application = LocalContext.current.applicationContext as Application
    val adsViewModel: AllRemindersAdsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AllRemindersAdsViewModel(application) as T
            }
        }
    )
    val adViews = adsViewModel.adViews.collectAsState().value

    LaunchedEffect(carId) { viewModel.send(AllRemindersIntent.SubscribeToReminders(carId)) }

    val context = LocalContext.current
    val carChooserSheet = rememberModalBottomSheetState()
    val reminderSheet = rememberModalBottomSheetState()
    val permissionsSheet = rememberModalBottomSheetState()
    val weekCalendarState = rememberWeekCalendarState(
        startDate = LocalDate.now().minusWeeks(1),
        endDate = LocalDate.now().plusWeeks(3),
        firstVisibleWeekDate = LocalDate.now(),
        firstDayOfWeek = DayOfWeek.MONDAY
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val carChooserSheetVisible = rememberSaveable { mutableStateOf(false) }
    var permissionsGranted by remember { mutableStateOf(checkPermissions(context)) }

    OnResume { permissionsGranted = checkPermissions(context) }

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.WithCalendar(
                    weekCalendarState = weekCalendarState,
                    currentDay = state.value.currentDate,
                    onDayClick = { viewModel.send(AllRemindersIntent.DateSelected(it)) },
                    withEvent = { state.value.remindersLocalDatesForScreenAsSet.contains(it) }
                ),
                title = currentCarName.value,
                navigationIconTint = GreenColor,
                navigationIcon = painterResource(R.drawable.my_car_arrow_back),
                navigationIconContentDescription = "Назад",
                onNavigationIconClick = {
                    if (!state.value.isLoading) navHostController.popBackStack()
                },
                actions = {
                    CarPhoto(
                        currentCarImageUri = currentCarImageUri,
                        isLightTheme = isLightTheme,
                        context = context
                    )
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = blackOrWhiteColor(isLightTheme)
                        ),
                        onClick = { carChooserSheetVisible.value = true }
                    ) {
                        Icon(
                            tint = blackOrWhiteColor(isLightTheme),
                            painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                            contentDescription = "Выбор машины"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            UiSnackbar(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                type = UiSnackbarType.Error
            )
        },
        floatingActionButton = {
            UiFAB(
                onClick = { navHostController.navigate(Screen.ReminderAddScreen.route) },
                icon = painterResource(R.drawable.my_car_add),
                iconContentDescription = "Иконка добавить",
                isVisible = !state.value.isLoading
            )
        }
    ) { topBarPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarPadding.calculateTopPadding())
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state.value.remindersForScreenAsList.isEmpty()) {
                true -> {
                    item { Spacer(modifier = Modifier.height(6.dp)) }
                    item {
                        Text(
                            text = "Пока нет данных о расходах. Вот несколько предложений для Вас:",
                            color = blackOrWhiteColor(isLightTheme),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp)
                                .padding(bottom = 16.dp)
                                .animateItem(tween(810, easing = FastOutSlowInEasing)),
                            textAlign = TextAlign.Center
                        )
                    }
                    items(adViews.size, key = { "$isLightTheme-$it" }) { index ->
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp)
                                .animateItem(tween(810, easing = FastOutSlowInEasing)),
                            factory = { context ->
                                NativeBannerView(context).apply {
                                    applyAppearance(nativeAdAppearance(isLightTheme))
                                    setAd(adViews[index])
                                }
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
                false -> {
                    items(items = state.value.remindersForScreenAsList, key = { it.id }) { reminder ->
                        UiReminderCard(
                            onClick = {
                                viewModel.send(
                                    AllRemindersIntent.ReminderBottomSheetVisibleChange(
                                        isVisible = true,
                                        reminderId = reminder.id,
                                        reminderText = reminder.text,
                                        reminderDate = reminder.date,
                                        reminderTime = reminder.time
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(tween(810, easing = FastOutSlowInEasing)),
                            isLightTheme = isLightTheme,
                            reminder = reminder
                        )
                    }
                }
            }
        }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = { viewModel.send(AllRemindersIntent.SubscribeToReminders(carId)) }
    )
    PermissionsBottomSheet(
        isLightTheme = isLightTheme,
        permissionsSheetState = permissionsSheet,
        navHostController = navHostController,
        context = context,
        onCancel = { permissionsGranted = true },
        isVisible = !permissionsGranted
    )
    ReminderBottomSheet(
        isLightTheme = isLightTheme,
        reminderSheetState = reminderSheet,
        viewModel = viewModel,
        state = state,
        navHostController = navHostController
    )
    CarChooserBottomSheet(
        isLightTheme = isLightTheme,
        allCars = allCars,
        carChooserSheet = carChooserSheet,
        carChooserSheetVisible = carChooserSheetVisible,
        currentCarId = carId,
        context = context,
        viewModel = viewModel
    )
    DeleteDialog(
        isLightTheme = isLightTheme,
        viewModel = viewModel,
        state = state,
        scope = scope,
        snackbarHostState = snackbarHostState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PermissionsBottomSheet(
    isLightTheme: Boolean,
    permissionsSheetState: SheetState,
    navHostController: NavHostController,
    isVisible: Boolean,
    onCancel: () -> Unit,
    context: Context
) {
    UiModalBottomSheet(
        onDismissRequest = {
            onCancel()
            navHostController.popBackStack()
        },
        sheetState = permissionsSheetState,
        isLightTheme = isLightTheme,
        isVisible = isVisible
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.my_car_reminder_photo),
                contentScale = ContentScale.Crop,
                contentDescription = "Фото для напоминаний"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Необходимо разрешение на уведомления",
                fontSize = 16.sp,
                color = grayColor(isLightTheme)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UiButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Разрешить",
                    onClick = {
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                            context.startActivity(this)
                        }
                    }
                )
                TextButton(
                    onClick = {
                        onCancel()
                        navHostController.popBackStack()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = darkGrayOrWhiteColor(isLightTheme),
                        contentColor = grayColor(isLightTheme)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 2.dp, vertical = 11.dp),
                        text = "Назад",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReminderBottomSheet(
    isLightTheme: Boolean,
    reminderSheetState: SheetState,
    navHostController: NavHostController,
    viewModel: AllRemindersViewModel,
    state: State<AllRemindersState>
) {
    UiModalBottomSheet(
        onDismissRequest = {
            viewModel.send(AllRemindersIntent.ReminderBottomSheetVisibleChange(isVisible = false))
        },
        sheetState = reminderSheetState,
        isLightTheme = isLightTheme,
        isVisible = state.value.reminderSheetVisible
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
                    text = state.value.reminderTextInBottomSheet ?: "",
                    fontSize = 16.sp,
                    color = blackOrWhiteColor(isLightTheme)
                )
                Text(
                    text = "${formatLocalDate(state.value.reminderDateInBottomSheet ?: LocalDate.now())} - ${
                        formatLocalTimeToString(state.value.reminderTimeInBottomSheet ?: LocalTime.now())
                    }",
                    fontSize = 14.sp,
                    color = grayColor(isLightTheme)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = {
                            navHostController.navigate(
                                Screen.ReminderScreen.passId(
                                    id = state.value.reminderIdInBottomSheet
                                        ?: throw IllegalArgumentException()
                                )
                            )
                            viewModel.send(
                                AllRemindersIntent.ReminderBottomSheetVisibleChange(
                                    isVisible = false
                                )
                            )
                        }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.my_car_edit),
                    tint = blackOrWhiteColor(isLightTheme),
                    contentDescription = "Карандаш (изменить)"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Изменить",
                    color = blackOrWhiteColor(isLightTheme),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        onClick = {
                            viewModel.send(AllRemindersIntent.DeleteDialogVisibleChange(true))
                        }
                    )
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
}

@Composable
private fun DeleteDialog(
    isLightTheme: Boolean,
    viewModel: AllRemindersViewModel,
    state: State<AllRemindersState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Удаление напоминания",
        subtitle = "Вы уверены? Это действие нельзя отменить!",
        yesTitle = "Удалить",
        yesTitleColor = RedColor,
        cancelTitle = "Отмена",
        cancelTitleColor = GreenColor,
        onDismissRequest = {
            viewModel.send(AllRemindersIntent.DeleteDialogVisibleChange(isVisible = false))
        },
        onYesClick = {
            viewModel.send(AllRemindersIntent.DeleteDialogVisibleChange(isVisible = false))
            val reminderId = state.value.reminderIdInBottomSheet
            viewModel.send(AllRemindersIntent.ReminderBottomSheetVisibleChange(isVisible = false))
            viewModel.send(
                AllRemindersIntent.DeleteReminder(
                    id = reminderId ?: throw IllegalArgumentException(),
                    onError = { msg ->
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
        },
        onCancelClick = {
            viewModel.send(AllRemindersIntent.DeleteDialogVisibleChange(isVisible = false))
        },
        isVisible = state.value.deleteDialogVisible
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarChooserBottomSheet(
    isLightTheme: Boolean,
    allCars: List<Car>,
    carChooserSheet: SheetState,
    carChooserSheetVisible: MutableState<Boolean>,
    viewModel: AllRemindersViewModel,
    currentCarId: Long,
    context: Context
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    UiModalBottomSheet(
        modifier = Modifier.padding(top = screenHeight / 3),
        onDismissRequest = { carChooserSheetVisible.value = false },
        sheetState = carChooserSheet,
        isLightTheme = isLightTheme,
        isVisible = carChooserSheetVisible.value
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            textAlign = TextAlign.Center,
            text = "Выбор текущего автомобиля",
            fontSize = 16.sp,
            color = grayColor(isLightTheme)
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }
            items(items = allCars, key = { it.id }) { car ->
                UiCarInBottomSheetCard(
                    onClick = {
                        carChooserSheetVisible.value = false
                        viewModel.send(AllRemindersIntent.CurrentCarChoose(car))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .animateItem(),
                    isLightTheme = isLightTheme,
                    isCurrentCar = currentCarId == car.id,
                    car = car,
                    context = context
                )
            }
            item { Spacer(modifier = Modifier.height(0.dp)) }
        }
    }
}

@Composable
private fun CarPhoto(
    currentCarImageUri: State<String?>,
    isLightTheme: Boolean,
    context: Context
) {
    if (!currentCarImageUri.value.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(currentCarImageUri.value)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.my_car_car_wo_photo),
            error = painterResource(R.drawable.my_car_car_wo_photo),
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f)
                ),
            contentScale = ContentScale.Crop,
            contentDescription = "Фото машины"
        )
    } else {
        Image(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f)
                ),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.my_car_car_wo_photo),
            contentDescription = "Иконка машины"
        )
    }
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