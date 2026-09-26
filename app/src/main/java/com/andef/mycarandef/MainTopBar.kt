package com.andef.mycarandef

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarTab
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.utils.currentDateRangeForTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    isLightTheme: Boolean,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navBackStackEntry: NavBackStackEntry?,
    context: Context,
    sheetVisible: MutableState<Boolean>,
    currentCarName: androidx.compose.runtime.State<String>,
    currentCarImageUri: androidx.compose.runtime.State<String?>,
    selectedTabIndex: MutableState<Int>,
    lastSelectedTabIndex: MutableState<Int>,
    startDate: MutableState<LocalDate>,
    endDate: MutableState<LocalDate>,
    datePickerVisible: MutableState<Boolean>,
    monthPickerVisible: MutableState<Boolean>,
    yearPickerVisible: MutableState<Boolean>,
    currentRoute: String?
) {
    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            (fadeIn(animationSpec = tween(800, easing = FastOutSlowInEasing)))
                .togetherWith(fadeOut(animationSpec = tween(800, easing = FastOutSlowInEasing)))
        }
    ) { state ->
        when (state == Screen.MainScreens.ExpensesMainScreen.route) {
            true -> {
                TopBarWithTabs(
                    isLightTheme = isLightTheme,
                    drawerState = drawerState,
                    scope = scope,
                    navBackStackEntry = navBackStackEntry,
                    context = context,
                    sheetVisible = sheetVisible,
                    currentCarName = currentCarName,
                    currentCarImageUri = currentCarImageUri,
                    selectedTabIndex = selectedTabIndex,
                    lastSelectedTabIndex = lastSelectedTabIndex,
                    startDate = startDate,
                    endDate = endDate,
                    datePickerVisible = datePickerVisible,
                    monthPickerVisible = monthPickerVisible,
                    yearPickerVisible = yearPickerVisible
                )
            }

            false -> UsualTopBar(
                isLightTheme = isLightTheme,
                drawerState = drawerState,
                scope = scope,
                navBackStackEntry = navBackStackEntry,
                context = context,
                sheetVisible = sheetVisible,
                currentCarName = currentCarName,
                currentCarImageUri = currentCarImageUri
            )
        }
    }
}

@Composable
private fun UsualTopBar(
    isLightTheme: Boolean,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navBackStackEntry: NavBackStackEntry?,
    context: Context,
    sheetVisible: MutableState<Boolean>,
    currentCarName: androidx.compose.runtime.State<String>,
    currentCarImageUri: androidx.compose.runtime.State<String?>
) {
    UiTopBar(
        isLightTheme = isLightTheme,
        type = UiTopBarType.NotCenter,
        title = currentCarName.value,
        navigationIcon = painterResource(R.drawable.my_car_menu),
        navigationIconContentDescription = "Меню",
        onNavigationIconClick = { scope.launch { drawerState.open() } },
        actions = {
            CarChooserActionButton(
                currentCarImageUri = currentCarImageUri,
                currentCarName = currentCarName.value,
                isLightTheme = isLightTheme,
                context = context,
                onClick = { sheetVisible.value = true }
            )
        },
        isVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes
    )
}

@Composable
private fun CarChooserActionButton(
    currentCarImageUri: State<String?>,
    currentCarName: String,
    isLightTheme: Boolean,
    context: Context,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.size(40.dp)) {
            CarPhoto(
                modifier = Modifier.align(Alignment.Center),
                currentCarImageUri = currentCarImageUri,
                isLightTheme = isLightTheme,
                context = context
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-1).dp, y = (-1).dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(darkGrayOrWhiteColor(isLightTheme))
                    .border(
                        width = 1.dp,
                        color = grayColor(isLightTheme).copy(alpha = 0.35f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    tint = blackOrWhiteColor(isLightTheme),
                    painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                    contentDescription = "Выбор машины: $currentCarName"
                )
            }
        }
    }
}

@Composable
private fun TopBarWithTabs(
    isLightTheme: Boolean,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navBackStackEntry: NavBackStackEntry?,
    context: Context,
    sheetVisible: MutableState<Boolean>,
    currentCarName: androidx.compose.runtime.State<String>,
    currentCarImageUri: androidx.compose.runtime.State<String?>,
    selectedTabIndex: MutableState<Int>,
    lastSelectedTabIndex: MutableState<Int>,
    startDate: MutableState<LocalDate>,
    endDate: MutableState<LocalDate>,
    datePickerVisible: MutableState<Boolean>,
    monthPickerVisible: MutableState<Boolean>,
    yearPickerVisible: MutableState<Boolean>
) {
    UiTopBar(
        isLightTheme = isLightTheme,
        type = UiTopBarType.WithTabs(
            tabs = dateTabs,
            selectedTabIndex = selectedTabIndex.value,
            onTabClick = {
                onDateTabClick(
                    selectedTabIndex = selectedTabIndex,
                    lastSelectedTabIndex = lastSelectedTabIndex,
                    startDate = startDate,
                    endDate = endDate,
                    tab = it,
                    datePickerVisible = datePickerVisible,
                    monthPickerVisible = monthPickerVisible,
                    yearPickerVisible = yearPickerVisible
                )
            }
        ),
        title = currentCarName.value,
        navigationIcon = painterResource(R.drawable.my_car_menu),
        navigationIconContentDescription = "Меню",
        onNavigationIconClick = { scope.launch { drawerState.open() } },
        actions = {
            CarChooserActionButton(
                currentCarImageUri = currentCarImageUri,
                currentCarName = currentCarName.value,
                isLightTheme = isLightTheme,
                context = context,
                onClick = { sheetVisible.value = true }
            )
        },
        isVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes
    )
}

fun onDateTabClick(
    selectedTabIndex: MutableState<Int>,
    lastSelectedTabIndex: MutableState<Int>,
    startDate: MutableState<LocalDate>,
    endDate: MutableState<LocalDate>,
    datePickerVisible: MutableState<Boolean>,
    monthPickerVisible: MutableState<Boolean>,
    yearPickerVisible: MutableState<Boolean>,
    tab: UiTopBarTab
) {
    when {
        tab.id == selectedTabIndex.value && tab.id == 2 -> {
            monthPickerVisible.value = true
        }

        tab.id == selectedTabIndex.value && tab.id == 3 -> {
            yearPickerVisible.value = true
        }

        tab.id != selectedTabIndex.value && tab.id in 0..3 -> {
            val range = currentDateRangeForTab(tab.id, LocalDate.now())
            selectedTabIndex.value = tab.id
            lastSelectedTabIndex.value = tab.id
            startDate.value = range.first
            endDate.value = range.second
        }

        tab.id == 4 -> {
            selectedTabIndex.value = tab.id
            datePickerVisible.value = true
        }
    }
}

val dateTabs = listOf(
    UiTopBarTab(id = 0, title = "День"),
    UiTopBarTab(id = 1, title = "Неделя"),
    UiTopBarTab(id = 2, title = "Месяц"),
    UiTopBarTab(id = 3, title = "Год"),
    UiTopBarTab(id = 4, title = "Период")
)

@Composable
private fun CarPhoto(
    modifier: Modifier = Modifier,
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
            modifier = modifier
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
            modifier = modifier
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
