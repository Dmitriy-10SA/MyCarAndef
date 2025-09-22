package com.andef.mycarandef.expense.presentation.expensemain

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.alertdialog.ui.UiAlertDialog
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.card.date.ui.UiDateAndAmountRow
import com.andef.mycarandef.design.card.expense.ui.UiExpenseCard
import com.andef.mycarandef.design.error.ui.UiError
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.RedColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.routes.Screen
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatPriceRuble
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseMainScreen(
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    currentCarId: Long,
    startDate: LocalDate,
    endDate: LocalDate,
    onLeftSwipe: () -> Unit,
    onRightSwipe: () -> Unit
) {
    val viewModel: ExpenseMainViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val loadAppSheetState = rememberModalBottomSheetState()
    val loadAppSheetVisible = remember { mutableStateOf(false) }
    val confirmAddToMyFinanceDialogVisible = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = state.value.initialFirstVisibleItemIndex,
        initialFirstVisibleItemScrollOffset = state.value.initialFirstVisibleItemScrollOffset
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.send(
                ExpenseMainIntent.SaveScrollState(
                    initialFirstVisibleItemIndex = listState.firstVisibleItemIndex,
                    initialFirstVisibleItemScrollOffset = listState.firstVisibleItemScrollOffset
                )
            )
        }
    }

    LaunchedEffect(currentCarId, startDate, endDate) {
        viewModel.send(ExpenseMainIntent.SubscribeForExpenses(currentCarId, startDate, endDate))
    }

    MainContent(
        viewModel = viewModel,
        state = state,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        currentCarId = currentCarId,
        startDate = startDate,
        endDate = endDate,
        listState = listState,
        onLeftSwipe = onLeftSwipe,
        onRightSwipe = onRightSwipe
    )
    BottomSheetWithDeleteDialog(
        navHostController = navHostController,
        viewModel = viewModel,
        sheetState = sheetState,
        isLightTheme = isLightTheme,
        state = state,
        scope = scope,
        snackbarHostState = snackbarHostState,
        loadAppSheetVisible = loadAppSheetVisible,
        confirmAddToMyFinanceDialogVisible = confirmAddToMyFinanceDialogVisible
    )
    LoadAppBottomSheet(LocalContext.current, loadAppSheetState, loadAppSheetVisible, isLightTheme)
    UiSnackbar(
        paddingValues = paddingValues,
        snackbarHostState = snackbarHostState,
        type = if (state.value.isErrorSnackbar) {
            UiSnackbarType.Error
        } else {
            UiSnackbarType.Success
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadAppBottomSheet(
    context: Context,
    loadAppSheetState: SheetState,
    loadAppSheetVisible: MutableState<Boolean>,
    isLightTheme: Boolean
) {
    UiModalBottomSheet(
        isLightTheme = isLightTheme,
        isVisible = loadAppSheetVisible.value,
        onDismissRequest = { loadAppSheetVisible.value = false },
        sheetState = loadAppSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Установите приложение Мои финансы:",
                color = grayColor(isLightTheme),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(0.dp))
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
                    icon = painterResource(R.drawable.my_finance_app_icon),
                    contentDescription = "Иконка мои финансы",
                    text = "Мои финансы",
                    onClick = {
                        Intent(
                            Intent.ACTION_VIEW,
                            "https://www.rustore.ru/catalog/app/com.andef.myfinance".toUri()
                        ).apply {
                            context.startActivity(this)
                        }
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
private fun ConfirmAddToMyFinanceDialog(
    confirmAddToMyFinanceDialogVisible: MutableState<Boolean>,
    viewModel: ExpenseMainViewModel,
    context: Context,
    expenseAmount: Double,
    expenseDate: LocalDate,
    expenseType: ExpenseType,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    loadAppSheetVisible: MutableState<Boolean>,
    isLightTheme: Boolean
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Добавление в мои финансы",
        subtitle = "Вы уверены? Если Вы уже добавляли этот расход, то он будет продублирован!",
        yesTitle = "Добавить",
        yesTitleColor = GreenColor,
        cancelTitle = "Отмена",
        cancelTitleColor = RedColor,
        onDismissRequest = { confirmAddToMyFinanceDialogVisible.value = false },
        onYesClick = {
            confirmAddToMyFinanceDialogVisible.value = false
            viewModel.send(
                ExpenseMainIntent.BottomSheetVisibleChange(isVisible = false)
            )
            viewModel.send(
                ExpenseMainIntent.AddToMyFinance(
                    context = context,
                    amount = expenseAmount,
                    date = expenseDate,
                    type = expenseType,
                    onSuccess = { msg ->
                        showSnackbar(scope, snackbarHostState, msg)
                    },
                    onAddError = { msg ->
                        showSnackbar(scope, snackbarHostState, msg)
                    },
                    onError = {
                        loadAppSheetVisible.value = true
                    }
                )
            )
        },
        onCancelClick = { confirmAddToMyFinanceDialogVisible.value = false },
        isVisible = confirmAddToMyFinanceDialogVisible.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetWithDeleteDialog(
    confirmAddToMyFinanceDialogVisible: MutableState<Boolean>,
    navHostController: NavHostController,
    viewModel: ExpenseMainViewModel,
    sheetState: SheetState,
    loadAppSheetVisible: MutableState<Boolean>,
    isLightTheme: Boolean,
    state: State<ExpenseMainState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    state.value.expenseIdInBottomSheet?.let { expenseId ->
        state.value.expenseTypeInBottomSheet?.let { expenseType ->
            state.value.expenseAmountInBottomSheet?.let { expenseAmount ->
                state.value.expenseDateInBottomSheet?.let { expenseDate ->
                    state.value.carIdForExpenseBottomSheet?.let { carId ->
                        UiModalBottomSheet(
                            onDismissRequest = {
                                viewModel.send(
                                    ExpenseMainIntent.BottomSheetVisibleChange(isVisible = false)
                                )
                            },
                            sheetState = sheetState,
                            isLightTheme = isLightTheme,
                            isVisible = state.value.showBottomSheet
                        ) {
                            BottomSheetContent(
                                isLightTheme = isLightTheme,
                                expenseType = expenseType,
                                expenseAmount = expenseAmount,
                                onDeleteClick = {
                                    viewModel.send(
                                        ExpenseMainIntent.ChangeDeleteDialogVisible(isVisible = true)
                                    )
                                },
                                expenseDate = expenseDate,
                                onAddToMyFinanceClick = {
                                    confirmAddToMyFinanceDialogVisible.value = true
                                },
                                onEditClick = {
                                    viewModel.send(
                                        ExpenseMainIntent.BottomSheetVisibleChange(isVisible = false)
                                    )
                                    navHostController.navigate(
                                        Screen.ExpenseScreen.passId(id = expenseId, carId = carId)
                                    )
                                }
                            )
                        }
                        ConfirmAddToMyFinanceDialog(
                            confirmAddToMyFinanceDialogVisible = confirmAddToMyFinanceDialogVisible,
                            viewModel = viewModel,
                            context = context,
                            expenseAmount = expenseAmount,
                            expenseDate = expenseDate,
                            expenseType = expenseType,
                            scope = scope,
                            snackbarHostState = snackbarHostState,
                            loadAppSheetVisible = loadAppSheetVisible,
                            isLightTheme = isLightTheme
                        )
                        DeleteDialog(
                            isLightTheme = isLightTheme,
                            expenseId = expenseId,
                            viewModel = viewModel,
                            scope = scope,
                            state = state,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

private fun showSnackbar(scope: CoroutineScope, snackbarHostState: SnackbarHostState, msg: String) {
    scope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(
            message = msg,
            withDismissAction = true
        )
    }
}

@Composable
private fun DeleteDialog(
    expenseId: Long,
    isLightTheme: Boolean,
    viewModel: ExpenseMainViewModel,
    state: State<ExpenseMainState>,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    UiAlertDialog(
        isLightTheme = isLightTheme,
        title = "Удаление расхода",
        subtitle = "Вы уверены? Это действие невозможно отменить!",
        yesTitle = "Удалить",
        yesTitleColor = RedColor,
        cancelTitle = "Отмена",
        cancelTitleColor = GreenColor,
        onDismissRequest = {
            viewModel.send(
                ExpenseMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        onYesClick = {
            viewModel.send(
                ExpenseMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
            viewModel.send(
                ExpenseMainIntent.BottomSheetVisibleChange(isVisible = false)
            )
            viewModel.send(
                ExpenseMainIntent.DeleteExpense(
                    expenseId = expenseId,
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
                ExpenseMainIntent.ChangeDeleteDialogVisible(isVisible = false)
            )
        },
        isVisible = state.value.deleteDialogVisible
    )
}

@Composable
private fun BottomSheetContent(
    isLightTheme: Boolean,
    expenseType: ExpenseType,
    expenseDate: LocalDate,
    expenseAmount: Double,
    onAddToMyFinanceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
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
                text = expenseType.title,
                fontSize = 16.sp,
                color = blackOrWhiteColor(isLightTheme)
            )
            Text(
                text = "${formatLocalDate(expenseDate)} - ${formatPriceRuble(expenseAmount)}",
                fontSize = 14.sp,
                color = grayColor(isLightTheme)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onAddToMyFinanceClick)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.my_finance_app_icon),
                tint = GreenColor,
                contentDescription = "Иконка Мои финансы"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Добавить в Мои финансы",
                color = GreenColor,
                fontSize = 16.sp
            )
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
    state: State<ExpenseMainState>,
    isLightTheme: Boolean,
    viewModel: ExpenseMainViewModel,
    currentCarId: Long,
    startDate: LocalDate,
    endDate: LocalDate,
    listState: LazyListState,
    onLeftSwipe: () -> Unit,
    onRightSwipe: () -> Unit
) {
    var totalDrag by remember { mutableStateOf(0f) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = { totalDrag = 0f },
                    onHorizontalDrag = { _, dragAmount ->
                        totalDrag += dragAmount
                    },
                    onDragEnd = {
                        if (totalDrag > 100) {
                            onRightSwipe()
                        } else if (totalDrag < -100) {
                            onLeftSwipe()
                        }
                    }
                )
            },
        state = listState,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        stickyHeader {
            UiDateAndAmountRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp),
                isIncome = false,
                totalAmount = state.value.totalExpenses,
                isLightTheme = isLightTheme,
                startDate = startDate,
                endDate = endDate
            )
        }
        item { Spacer(modifier = Modifier.height(6.dp)) }
        state.value.expenses.forEach { expensesForLazyColumn ->
            item(key = "date-${expensesForLazyColumn.date}") {
                Spacer(modifier = Modifier.height(18.dp))
                UiDateAndAmountRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp)
                        .animateItem(tween(800, easing = FastOutSlowInEasing)),
                    isIncome = false,
                    isLightTheme = isLightTheme,
                    date = expensesForLazyColumn.date,
                    amount = expensesForLazyColumn.totalAmount
                )
            }
            items(items = expensesForLazyColumn.expenses, key = { it.id }) { expense ->
                UiExpenseCard(
                    onClick = {
                        viewModel.send(
                            ExpenseMainIntent.BottomSheetVisibleChange(
                                isVisible = true,
                                expenseType = expense.type,
                                expenseAmount = expense.amount,
                                expenseId = expense.id,
                                expenseDate = expense.date,
                                carId = expense.carId
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(tween(800, easing = FastOutSlowInEasing)),
                    isLightTheme = isLightTheme,
                    expense = expense
                )
            }
        }
        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiError(
        isVisible = state.value.isError,
        paddingValues = paddingValues,
        isLightTheme = isLightTheme,
        onRetry = {
            viewModel.send(
                ExpenseMainIntent.SubscribeForExpenses(
                    currentCarId,
                    startDate,
                    endDate
                )
            )
        }
    )
}