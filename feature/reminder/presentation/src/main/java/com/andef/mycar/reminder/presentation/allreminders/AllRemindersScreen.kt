package com.andef.mycar.reminder.presentation.allreminders

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import java.time.DayOfWeek
import java.time.LocalDate

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
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val weekCalendarState = rememberWeekCalendarState(
        startDate = LocalDate.now().minusWeeks(1),
        endDate = LocalDate.now().plusWeeks(2),
        firstVisibleWeekDate = LocalDate.now(),
        firstDayOfWeek = DayOfWeek.MONDAY
    )
    var selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val sheetVisible = rememberSaveable { mutableStateOf(false) }

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.WithCalendar(
                    weekCalendarState = weekCalendarState,
                    currentDay = selectedDate.value,
                    onDayClick = { selectedDate.value = it },
                    withEvent = { it == LocalDate.now().plusDays(2) }
                ),
                title = currentCarName.value,
                navigationIcon = painterResource(R.drawable.my_car_arrow_back),
                navigationIconContentDescription = "Назад",
                onNavigationIconClick = {
                    //if (!state.value.isLoading) navHostController.popBackStack()
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
                        onClick = { sheetVisible.value = true }
                    ) {
                        Icon(
                            tint = if (isLightTheme) Black else White,
                            painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                            contentDescription = "Выбор машины"
                        )
                    }
                }
            )
        }
    ) { topBarPadding ->
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