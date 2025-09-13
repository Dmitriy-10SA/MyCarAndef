package com.andef.mycarandef

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.andef.mycar.core.ads.InterstitialAdManager
import com.andef.mycarandef.common.MyCarComponent
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.BlackColor
import com.andef.mycarandef.design.theme.DarkGrayColor
import com.andef.mycarandef.design.theme.GrayForDarkColor
import com.andef.mycarandef.design.theme.GrayForLightColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.routes.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModalDrawerSheetContent(
    isLightTheme: Boolean,
    username: String?,
    drawerState: DrawerState,
    scope: CoroutineScope,
    component: MyCarComponent,
    navHostController: NavHostController,
    interstitialAdManager: InterstitialAdManager
) {
    val nameChangeSheetState = rememberModalBottomSheetState()
    val nameChangeSheetVisible = rememberSaveable { mutableStateOf(false) }
    var usernameValue by remember { mutableStateOf(username ?: "") }

    val feedbackSheetState = rememberModalBottomSheetState()
    val feedbackSheetVisible = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    ModalDrawerSheet(
        drawerState = drawerState,
        drawerShape = RoundedCornerShape(
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp,
            topStart = 0.dp
        ),
        drawerContainerColor = darkGrayOrWhiteColor(isLightTheme),
        drawerContentColor = blackOrWhiteColor(isLightTheme)
    ) {
        InnerContent(
            isLightTheme = isLightTheme,
            component = component,
            username = username,
            nameChangeSheetVisible = nameChangeSheetVisible,
            navHostController = navHostController,
            scope = scope,
            drawerState = drawerState,
            context = context,
            feedbackSheetVisible = feedbackSheetVisible,
            interstitialAdManager = interstitialAdManager
        )
    }
    UiModalBottomSheet(
        isLightTheme = isLightTheme,
        isVisible = nameChangeSheetVisible.value,
        onDismissRequest = { nameChangeSheetVisible.value = false },
        sheetState = nameChangeSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            UiTextField(
                isLightTheme = isLightTheme,
                value = usernameValue,
                onValueChange = { usernameValue = it },
                modifier = Modifier.fillMaxWidth(),
                placeholderText = "Ваше имя",
                leadingIcon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_person),
                contentDescription = "Иконка человека",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
            UiButton(
                text = "Сохранить",
                onClick = {
                    nameChangeSheetVisible.value = false
                    component.setUsernameUseCase.invoke(usernameValue)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = usernameValue.isNotEmpty()
            )
        }
    }
    UiModalBottomSheet(
        isLightTheme = isLightTheme,
        isVisible = feedbackSheetVisible.value,
        onDismissRequest = { feedbackSheetVisible.value = false },
        sheetState = feedbackSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Нашли ошибку или есть предложения по улучшению? Напишите разработчику:",
                color = grayColor(isLightTheme),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
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
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_telegram_icon),
                    contentDescription = "Иконка телеграмм",
                    text = "Telegram",
                    onClick = {
                        Intent(Intent.ACTION_VIEW, "https://t.me/dsemkin".toUri()).apply {
                            context.startActivity(this)
                        }
                    }
                )
                AppItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_mail_icon),
                    contentDescription = "Иконка почты",
                    text = "Mail",
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:semkin_dmitriy10@vk.com".toUri()
                        }
                        context.startActivity(
                            Intent.createChooser(
                                emailIntent,
                                "Выберите почтовый клиент"
                            )
                        )
                    }
                )
            }
        }
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
                .padding(3.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .size(65.dp)
                .background(color = WhiteColor, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(all = 7.dp),
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

@Composable
private fun InnerContent(
    isLightTheme: Boolean,
    username: String?,
    navHostController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    context: Context,
    nameChangeSheetVisible: MutableState<Boolean>,
    feedbackSheetVisible: MutableState<Boolean>,
    component: MyCarComponent,
    interstitialAdManager: InterstitialAdManager
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(3.dp))
        Icon(
            modifier = Modifier.size(100.dp),
            tint = blackOrWhiteColor(isLightTheme),
            painter = painterResource(com.andef.mycarandef.design.R.drawable.my_car_car_key),
            contentDescription = "Иконка приложения"
        )
        UsernameContent(
            isLightTheme = isLightTheme,
            username = username,
            nameChangeSheetVisible = nameChangeSheetVisible
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                InnerContentItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_analytics),
                    iconContentDescription = "Иконка диаграмма",
                    itemText = "Анализ трат",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navHostController.navigate(Screen.ExpenseAnalysisScreen.route)
                        }
                    }
                )
            }
            item {
                InnerContentItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_reminder),
                    iconContentDescription = "Иконка звонка",
                    itemText = "Напоминания",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            navHostController.navigate(Screen.AllRemindersScreen.route)
                        }
                    }
                )
            }
            item {
                InnerContentItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_backup),
                    iconContentDescription = "Иконка резервное копирование",
                    itemText = "Резервное копирование",
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            interstitialAdManager.showAd {
                                navHostController.navigate(Screen.BackupMainScreen.route)
                            }
                        }
                    }
                )
            }
            item {
                InnerContentItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(com.andef.mycarandef.design.R.drawable.my_car_feedback),
                    iconContentDescription = "Иконка почты",
                    itemText = "Обратная связь",
                    onClick = { feedbackSheetVisible.value = true }
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
        )
        UiThemeContent(isLightTheme = isLightTheme, component = component)
        Spacer(modifier = Modifier.height(0.dp))
    }
}

@Composable
private fun InnerContentItem(
    isLightTheme: Boolean,
    itemText: String,
    icon: Painter,
    iconContentDescription: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = icon,
            contentDescription = iconContentDescription
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = itemText,
            fontSize = 18.sp,
            color = blackOrWhiteColor(isLightTheme)
        )
    }
}

@Composable
private fun ColumnScope.UsernameContent(
    isLightTheme: Boolean,
    username: String?,
    nameChangeSheetVisible: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            maxLines = 1,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            text = username ?: "",
            fontSize = 22.sp,
            color = blackOrWhiteColor(isLightTheme)
        )
        IconButton(
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent,
                contentColor = blackOrWhiteColor(isLightTheme)
            ),
            onClick = { nameChangeSheetVisible.value = true }
        ) {
            Icon(
                painter = painterResource(com.andef.mycarandef.design.R.drawable.my_car_edit),
                contentDescription = "Карандаш (изменить)"
            )
        }
    }
}

@Composable
private fun ColumnScope.UiThemeContent(isLightTheme: Boolean, component: MyCarComponent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .height(48.dp)
            .background(
                color = grayColor(isLightTheme).copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(end = 2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLightTheme) WhiteColor else Color.Transparent,
                contentColor = blackOrWhiteColor(isLightTheme)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = if (isLightTheme) {
                    GrayForLightColor.copy(alpha = 0.3f)
                } else {
                    Color.Transparent
                }
            ),
            onClick = {
                if (!isLightTheme) {
                    component.setThemeUseCase.invoke(isLightTheme = true)
                }
            }
        ) {
            Icon(
                painter = if (isLightTheme) {
                    painterResource(com.andef.mycarandef.design.R.drawable.my_car_baseline_light)
                } else {
                    painterResource(com.andef.mycarandef.design.R.drawable.my_car_outline_light)
                },
                tint = if (isLightTheme) BlackColor else GrayForDarkColor,
                contentDescription = "Значок для светлой темы"
            )
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .padding(start = 2.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLightTheme) Color.Transparent else DarkGrayColor,
                contentColor = blackOrWhiteColor(isLightTheme)
            ),
            onClick = {
                if (isLightTheme) {
                    component.setThemeUseCase.invoke(isLightTheme = false)
                }
            },
            border = BorderStroke(
                width = 1.dp,
                color = if (isLightTheme) {
                    Color.Transparent
                } else {
                    GrayForDarkColor.copy(alpha = 0.3f)
                }
            )
        ) {
            Icon(
                painter = if (isLightTheme) {
                    painterResource(com.andef.mycarandef.design.R.drawable.my_car_outline_dark)
                } else {
                    painterResource(com.andef.mycarandef.design.R.drawable.my_car_baseline_dark)
                },
                tint = if (isLightTheme) GrayForLightColor else WhiteColor,
                contentDescription = "Значок для темной темы"
            )
        }
    }
}