package com.andef.mycarandef

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarTab
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.routes.Screen
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
                    datePickerVisible = datePickerVisible
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
                onClick = { sheetVisible.value = true }
            ) {
                Icon(
                    tint = blackOrWhiteColor(isLightTheme),
                    painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                    contentDescription = "Выбор машины"
                )
            }
        },
        isVisible = navBackStackEntry?.destination?.route in Screen.MainScreens.allRoutes
    )
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
    datePickerVisible: MutableState<Boolean>
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
                    datePickerVisible = datePickerVisible
                )
            }
        ),
        title = currentCarName.value,
        navigationIcon = painterResource(R.drawable.my_car_menu),
        navigationIconContentDescription = "Меню",
        onNavigationIconClick = { scope.launch { drawerState.open() } },
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
                onClick = { sheetVisible.value = true }
            ) {
                Icon(
                    tint = blackOrWhiteColor(isLightTheme),
                    painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                    contentDescription = "Выбор машины"
                )
            }
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
    tab: UiTopBarTab
) {
    if (tab.id != selectedTabIndex.value || tab.id == 5) {
        val now = LocalDate.now()
        val newLastTabIndexAndDates: Pair<Int, Pair<LocalDate, LocalDate>>? = when (tab.id) {
            0 -> tab.id to (now to now)
            1 -> tab.id to (now.minusDays(7) to now)
            2 -> tab.id to (now.minusMonths(1) to now)
            3 -> tab.id to (now.minusMonths(6) to now)
            4 -> tab.id to (now.minusYears(1) to now)
            else -> null
        }
        if (newLastTabIndexAndDates != null) {
            selectedTabIndex.value = tab.id
            lastSelectedTabIndex.value = newLastTabIndexAndDates.first
            startDate.value = newLastTabIndexAndDates.second.first
            endDate.value = newLastTabIndexAndDates.second.second
        } else {
            selectedTabIndex.value = tab.id
            datePickerVisible.value = true
        }
    }
}

val dateTabs = listOf(
    UiTopBarTab(id = 0, title = "День"),
    UiTopBarTab(id = 1, title = "Неделя"),
    UiTopBarTab(id = 2, title = "Месяц"),
    UiTopBarTab(id = 3, title = "Полгода"),
    UiTopBarTab(id = 4, title = "Год"),
    UiTopBarTab(id = 5, title = "Период")
)

@Composable
private fun CarPhoto(
    currentCarImageUri: androidx.compose.runtime.State<String?>,
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