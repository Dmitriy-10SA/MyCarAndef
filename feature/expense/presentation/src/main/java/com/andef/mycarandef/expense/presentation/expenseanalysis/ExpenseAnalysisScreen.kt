package com.andef.mycarandef.expense.presentation.expenseanalysis

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.car.ui.UiCarInBottomSheetCard
import com.andef.mycarandef.design.datepicker.ui.UiRangeDatePickerDialog
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarTab
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatPriceRuble
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAnalysisScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarName: State<String>,
    allCars: List<Car>,
    currentCarImageUri: State<String?>,
    carId: Long
) {
    val viewModel: ExpenseAnalysisViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val sheetVisible = rememberSaveable { mutableStateOf(false) }
    val legendScrollState = rememberScrollState()

    LaunchedEffect(carId) { viewModel.send(ExpenseAnalysisIntent.LoadExpenses(carId)) }

    UiScaffold(
        isLightTheme = isLightTheme,
        topBar = {
            UiTopBar(
                isLightTheme = isLightTheme,
                type = UiTopBarType.WithTabs(
                    tabs = dateTabs,
                    selectedTabIndex = state.value.selectedDateTabId,
                    onTabClick = { tab ->
                        viewModel.send(ExpenseAnalysisIntent.SelectedTabIdChange(tab.id))
                        val now = LocalDate.now()
                        if (tab.id == dateTabs[0].id) {
                            viewModel.send(
                                ExpenseAnalysisIntent.DatesChange(
                                    startDate = now, endDate = now
                                )
                            )
                        } else if (tab.id == dateTabs[1].id) {
                            viewModel.send(
                                ExpenseAnalysisIntent.DatesChange(
                                    startDate = now.minusWeeks(1), endDate = now
                                )
                            )
                        } else if (tab.id == dateTabs[2].id) {
                            viewModel.send(
                                ExpenseAnalysisIntent.DatesChange(
                                    startDate = now.minusMonths(1), endDate = now
                                )
                            )
                        } else if (tab.id == dateTabs[3].id) {
                            viewModel.send(
                                ExpenseAnalysisIntent.DatesChange(
                                    startDate = now.minusMonths(6), endDate = now
                                )
                            )
                        } else if (tab.id == dateTabs[4].id) {
                            viewModel.send(
                                ExpenseAnalysisIntent.DatesChange(
                                    startDate = now.minusYears(1), endDate = now
                                )
                            )
                        } else {
                            viewModel.send(ExpenseAnalysisIntent.RangePickerVisibleChange(true))
                        }
                    }
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
                        onClick = { sheetVisible.value = true }
                    ) {
                        Icon(
                            tint = blackOrWhiteColor(isLightTheme),
                            painter = painterResource(R.drawable.my_car_keyboard_arrow_down),
                            contentDescription = "Выбор машины"
                        )
                    }
                }
            )
        }
    ) { topBarPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topBarPadding.calculateTopPadding())
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            state.value.totalSumForScreen?.let { sum ->
                state.value.expensesInfoForScreen.let { expensesInfo ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val dateText =
                            if (state.value.startDate == state.value.endDate) {
                                formatLocalDate(state.value.startDate)
                            } else {
                                "${formatLocalDate(state.value.startDate)} " +
                                        "- ${formatLocalDate(state.value.endDate)}"
                            }
                        Text(
                            text = dateText,
                            fontSize = 14.sp,
                            color = grayColor(isLightTheme),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (sum == 0.0) formatPriceRuble(sum) else "-${formatPriceRuble(sum)}",
                            fontSize = 18.sp,
                            color = blackOrWhiteColor(isLightTheme),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    val slices = mutableListOf<PieChartData.Slice>().apply {
                        Expense.allExpenseTypes.forEach { type ->
                            add(
                                PieChartData.Slice(
                                    value = expensesInfo[type]?.first ?: 0.0f,
                                    color = getColorForExpenseType(type)
                                )
                            )
                        }
                    }
                    PieChart(
                        modifier = Modifier.size(300.dp),
                        pieChartData = PieChartData(slices = slices),
                        animation = simpleChartAnimation(),
                        sliceDrawer = SimpleSliceDrawer()
                    )
                    Expense.allExpenseTypes.forEachIndexed { index, type ->
                        Spacer(modifier = Modifier.height(20.dp))
                        LegendRow(
                            isLightTheme = isLightTheme,
                            color = getColorForExpenseType(type),
                            title = type.title,
                            percent = expensesInfo[type]?.first ?: 0.0f,
                            amount = expensesInfo[type]?.second ?: 0.0,
                            scrollState = legendScrollState
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = { viewModel.send(ExpenseAnalysisIntent.LoadExpenses(carId)) }
    )
    UiRangeDatePickerDialog(
        isVisible = state.value.dateRangePickerVisible,
        isLightTheme = isLightTheme,
        onDismissRequest = {
            viewModel.send(ExpenseAnalysisIntent.ChooseLastSelectedTabId)
            viewModel.send(ExpenseAnalysisIntent.RangePickerVisibleChange(false))
        },
        onOkClick = { startDate, endDate ->
            viewModel.send(ExpenseAnalysisIntent.RangePickerVisibleChange(false))
            viewModel.send(
                ExpenseAnalysisIntent.DatesChange(
                    startDate = startDate,
                    endDate = endDate
                )
            )
        }
    )
    BottomSheet(
        isLightTheme = isLightTheme,
        allCars = allCars,
        sheetState = sheetState,
        sheetVisible = sheetVisible,
        currentCarId = carId,
        context = context,
        viewModel = viewModel
    )
}

private fun getColorForExpenseType(type: ExpenseType): Color {
    return when (type) {
        ExpenseType.FUEL -> Color(0xFFFF6B6B)
        ExpenseType.WORKS -> Color(0xFF4BCFA9)
        ExpenseType.WASHING -> Color(0xFF4A9FF5)
        ExpenseType.OTHER -> Color(0xFFFFD166)
    }
}

@Composable
private fun LegendRow(
    isLightTheme: Boolean,
    color: Color,
    title: String,
    percent: Float,
    amount: Double,
    scrollState: ScrollState
) {
    val price = if (amount == 0.0) formatPriceRuble(amount) else "-${formatPriceRuble(amount)}"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Text(
            text = "$title (${String.format(Locale.US, "%.2f", percent)}%)",
            fontSize = 16.sp,
            color = grayColor(isLightTheme),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = price,
            color = blackOrWhiteColor(isLightTheme),
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
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

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    isLightTheme: Boolean,
    allCars: List<Car>,
    sheetState: SheetState,
    sheetVisible: MutableState<Boolean>,
    viewModel: ExpenseAnalysisViewModel,
    currentCarId: Long,
    context: Context
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    UiModalBottomSheet(
        modifier = Modifier.padding(top = screenHeight / 3),
        onDismissRequest = { sheetVisible.value = false },
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        isVisible = sheetVisible.value
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
                        sheetVisible.value = false
                        viewModel.send(ExpenseAnalysisIntent.CurrentCarChoose(car))
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

private val dateTabs = listOf(
    UiTopBarTab(id = 0, title = "День"),
    UiTopBarTab(id = 1, title = "Неделя"),
    UiTopBarTab(id = 2, title = "Месяц"),
    UiTopBarTab(id = 3, title = "Полгода"),
    UiTopBarTab(id = 4, title = "Год"),
    UiTopBarTab(id = 5, title = "Период")
)