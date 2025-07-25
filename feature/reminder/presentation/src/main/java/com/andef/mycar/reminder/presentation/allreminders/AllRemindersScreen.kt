package com.andef.mycar.reminder.presentation.allreminders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.Red
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatLocalTimeToString
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
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
                            contentColor = if (isLightTheme) Black else White
                        ),
                        onClick = { carChooserSheetVisible.value = true }
                    ) {
                        Icon(
                            tint = if (isLightTheme) Black else White,
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
                .padding(horizontal = 12.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }
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
                        .animateItem(),
                    isLightTheme = isLightTheme,
                    reminder = reminder
                )
            }
            item { Spacer(modifier = Modifier.height(0.dp)) }
        }
    }
    UiLoading(
        isVisible = state.value.isLoading,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        withTouch = false
    )
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
                color = if (isLightTheme) GrayForLight else GrayForDark
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
                        containerColor = if (isLightTheme) White else DarkGray,
                        contentColor = if (isLightTheme) GrayForLight else GrayForDark
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
                    color = if (isLightTheme) Black else White
                )
                Text(
                    text = "${formatLocalDate(state.value.reminderDateInBottomSheet ?: LocalDate.now())} - ${
                        formatLocalTimeToString(state.value.reminderTimeInBottomSheet ?: LocalTime.now())
                    }",
                    fontSize = 14.sp,
                    color = if (isLightTheme) GrayForLight else GrayForDark
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
                    tint = if (isLightTheme) Black else White,
                    contentDescription = "Карандаш (изменить)"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Изменить",
                    color = if (isLightTheme) Black else White,
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
                    tint = Red,
                    contentDescription = "Корзина"
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Удалить", color = Red, fontSize = 16.sp)
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
        title = "Вы уверены?",
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
            color = if (isLightTheme) GrayForLight else GrayForDark
        )
        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = if (isLightTheme) Black.copy(alpha = 0.2f) else White.copy(alpha = 0.2f)
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
                    color = if (isLightTheme) {
                        GrayForLight.copy(alpha = 0.3f)
                    } else {
                        GrayForDark.copy(alpha = 0.3f)
                    }
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
                    color = if (isLightTheme) {
                        GrayForLight.copy(alpha = 0.3f)
                    } else {
                        GrayForDark.copy(alpha = 0.3f)
                    }
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