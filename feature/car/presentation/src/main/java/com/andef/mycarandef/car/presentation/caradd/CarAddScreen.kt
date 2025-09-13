package com.andef.mycarandef.car.presentation.caradd

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.chooser.ui.UiChooser
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.utils.YearVisualTransformation
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CarAddScreen(
    carId: Long?,
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long
) {
    val viewModel: CarAddViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flags)
                viewModel.send(CarAddIntent.ChangePhoto(uri.toString()))
            }
        }
    )

    LaunchedEffect(Unit) {
        carId?.let {
            viewModel.send(
                CarAddIntent.InitCarByLateCar(
                    carId = carId,
                    onError = { msg ->
                        scope.launch {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = msg,
                                withDismissAction = true
                            )
                            navHostController.popBackStack()
                        }
                    }
                )
            )
        }
    }

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.Center,
                title = "Гараж",
                navigationIconTint = GreenColor,
                navigationIcon = painterResource(R.drawable.my_car_arrow_back),
                navigationIconContentDescription = "Назад",
                onNavigationIconClick = {
                    if (!state.value.isLoading) navHostController.popBackStack()
                }
            )
        },
        snackbarHost = {
            UiSnackbar(
                paddingValues = paddingValues,
                snackbarHostState = snackbarHostState,
                type = UiSnackbarType.Error
            )
        }
    ) { topBarPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarPadding.calculateTopPadding())
                .navigationBarsPadding()
                .imePadding()
        ) {
            MainContent(
                isLightTheme = isLightTheme,
                scrollState = scrollState,
                state = state,
                viewModel = viewModel,
                pickImageLauncher = pickImageLauncher,
                context = context
            )
            DownButton(
                isLightTheme = isLightTheme,
                keyboard = keyboard,
                viewModel = viewModel,
                state = state,
                navHostController = navHostController,
                scope = scope,
                snackbarHostState = snackbarHostState,
                currentCarId = currentCarId
            )
        }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    BackHandler { if (!state.value.isLoading) navHostController.popBackStack() }
}

@Composable
private fun ColumnScope.MainContent(
    isLightTheme: Boolean,
    scrollState: ScrollState,
    state: State<CarAddState>,
    pickImageLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>,
    viewModel: CarAddViewModel,
    context: Context
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        CarPhoto(state = state, isLightTheme = isLightTheme, context = context)
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Обязательные поля:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = grayColor(isLightTheme),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.brand,
            onValueChange = { viewModel.send(CarAddIntent.ChangeBrand(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Марка",
            leadingIcon = painterResource(R.drawable.my_car_brand),
            contentDescription = "Значок бренд",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.model,
            onValueChange = { viewModel.send(CarAddIntent.ChangeModel(it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Модель",
            leadingIcon = painterResource(R.drawable.my_car_car),
            contentDescription = "Значок авто",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Необязательные поля:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            color = grayColor(isLightTheme),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiChooser(
            isLightTheme = isLightTheme,
            value = state.value.photo ?: "",
            onClick = {
                pickImageLauncher.launch(arrayOf("image/*"))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Фото",
            leadingIcon = painterResource(R.drawable.my_car_image),
            leadingIconContentDescription = "Значок фото",
            trailingIcon = painterResource(R.drawable.my_car_attach),
            trailingIconContentDescription = "Значок скрепки"
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.year?.toString() ?: "",
            onValueChange = {
                try {
                    val year = it.filter { it.isDigit() }.take(4).toInt()
                    viewModel.send(CarAddIntent.ChangeYear(year))
                } catch (_: Exception) {
                    viewModel.send(CarAddIntent.ChangeYear(null))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Год выпуска",
            leadingIcon = painterResource(R.drawable.my_car_schedule),
            contentDescription = "Значок часов",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next
            ),
            visualTransformation = YearVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.registrationMark ?: "",
            onValueChange = { viewModel.send(CarAddIntent.ChangeRegistrationMark(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            placeholderText = "Госномер",
            leadingIcon = painterResource(R.drawable.my_car_more_horiz),
            contentDescription = "Три горизонтальные точки",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun ColumnScope.CarPhoto(
    state: State<CarAddState>,
    isLightTheme: Boolean,
    context: Context
) {
    if (!state.value.photo.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(state.value.photo)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.my_car_car_cars),
            error = painterResource(R.drawable.my_car_car_cars),
            modifier = Modifier
                .padding(top = 12.dp)
                .size(130.dp)
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
                .padding(top = 12.dp)
                .size(130.dp)
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f)
                ),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.my_car_car_cars),
            contentDescription = "Иконка машины"
        )
    }
}


@Composable
private fun ColumnScope.DownButton(
    isLightTheme: Boolean,
    keyboard: SoftwareKeyboardController?,
    viewModel: CarAddViewModel,
    navHostController: NavHostController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    state: State<CarAddState>,
    currentCarId: Long
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = blackOrWhiteColor(isLightTheme).copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        UiButton(
            text = "Сохранить",
            onClick = {
                keyboard?.hide()
                viewModel.send(
                    CarAddIntent.SaveClick(
                        onSuccess = navHostController::popBackStack,
                        currentCarId = currentCarId,
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .imePadding(),
            enabled = state.value.saveButtonEnabled && !state.value.isLoading
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}