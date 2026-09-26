package com.andef.mycarandef.expense.presentation.expenseadd

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
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
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.button.ui.UiButton
import com.andef.mycarandef.design.card.expense.ui.getImageForExpense
import com.andef.mycarandef.design.chooser.ui.UiChooser
import com.andef.mycarandef.design.datepicker.ui.UiDatePickerDialog
import com.andef.mycarandef.design.loading.ui.UiLoading
import com.andef.mycarandef.design.menu.ui.UiMenu
import com.andef.mycarandef.design.scaffold.ui.UiScaffold
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.snackbar.ui.UiSnackbar
import com.andef.mycarandef.design.textfield.ui.UiTextField
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.design.topbar.type.UiTopBarType
import com.andef.mycarandef.design.topbar.ui.UiTopBar
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.utils.formatAmountForEdit
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.normalizeAmountInput
import com.andef.mycarandef.utils.parseAmountToKopecks
import com.andef.mycarandef.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAddScreen(
    expenseId: Long?,
    navHostController: NavHostController,
    viewModelFactory: ViewModelFactory,
    paddingValues: PaddingValues,
    isLightTheme: Boolean,
    carId: Long
) {
    val viewModel: ExpenseAddViewModel = viewModel(factory = viewModelFactory)
    val state = viewModel.state.collectAsState()

    val keyboard = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.send(
            ExpenseAddIntent.InitDefaultType(applyToExpense = expenseId == null)
        )
        if (expenseId != null) {
            viewModel.send(
                ExpenseAddIntent.InitExpenseByLateExpense(
                    expenseId = expenseId,
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
                title = "Траты",
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
                viewModel = viewModel
            )
            DownButton(
                isLightTheme = isLightTheme,
                keyboard = keyboard,
                viewModel = viewModel,
                state = state,
                navHostController = navHostController,
                scope = scope,
                snackbarHostState = snackbarHostState,
                carId = carId
            )
        }
    }
    UiLoading(isVisible = state.value.isLoading, isLightTheme = isLightTheme)
    UiDatePickerDialog(
        isVisible = state.value.datePickerVisible,
        isLightTheme = isLightTheme,
        onDismissRequest = { viewModel.send(ExpenseAddIntent.ChangeDatePickerVisible(false)) },
        onOkClick = { date ->
            viewModel.send(ExpenseAddIntent.ChangeDate(date))
            viewModel.send(ExpenseAddIntent.ChangeDatePickerVisible(false))
        }
    )
    BackHandler { if (!state.value.isLoading) navHostController.popBackStack() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.MainContent(
    isLightTheme: Boolean,
    scrollState: ScrollState,
    state: State<ExpenseAddState>,
    viewModel: ExpenseAddViewModel
) {
    var localAmount by rememberSaveable { mutableStateOf("") }
    var initializedAmount by rememberSaveable { mutableStateOf<Long?>(null) }
    LaunchedEffect(state.value.amount) {
        val amount = state.value.amount
        if (amount != null && localAmount.isBlank() && initializedAmount != amount) {
            localAmount = formatAmountForEdit(amount)
            initializedAmount = amount
        }
    }
    var typeExpanded by remember { mutableStateOf(false) }
    var defaultTypeExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(6.dp))
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
            painter = painterResource(R.drawable.my_car_piechart_expenses),
            contentDescription = "Иконка траты диаграмма круговая"
        )
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
            value = localAmount,
            onValueChange = { newText ->
                val normalized = normalizeAmountInput(newText)
                localAmount = normalized
                val parsed = parseAmountToKopecks(normalized)
                viewModel.send(ExpenseAddIntent.ChangeAmount(parsed))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Сумма (₽)",
            leadingIcon = painterResource(R.drawable.my_car_ruble),
            contentDescription = "Значок рубля",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            UiMenu(
                items = Expense.allExpenseTypes,
                modifier = Modifier.fillMaxWidth(),
                itemToString = { item -> item.title },
                itemToLeadingIcon = { item -> ExpenseTypeIcon(item) },
                isLightTheme = isLightTheme,
                value = state.value.type?.title ?: "",
                placeholderText = "Тип",
                textFieldLeadingIcon = painterResource(R.drawable.my_car_more_horiz),
                textFieldLeadingIconContentDescription = "Три горизонтальные точки",
                onItemClick = { item ->
                    typeExpanded = false
                    viewModel.send(ExpenseAddIntent.ChangeType(item))
                },
                onExpandedChange = { typeExpanded = it },
                expanded = typeExpanded,
                onLongClick = {
                    typeExpanded = false
                    defaultTypeExpanded = true
                }
            )
            DropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, grayColor(isLightTheme), shape = RoundedCornerShape(16.dp)),
                expanded = defaultTypeExpanded,
                onDismissRequest = { defaultTypeExpanded = false },
                containerColor = darkGrayOrWhiteColor(isLightTheme)
            ) {
                Text(
                    text = "Тип расхода по умолчанию",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = blackOrWhiteColor(isLightTheme),
                    fontSize = 16.sp
                )
                HorizontalDivider(color = grayColor(isLightTheme).copy(alpha = 0.3f))
                DefaultTypeMenuItem(
                    title = "Не выбирать автоматически",
                    selected = state.value.defaultType == null,
                    isLightTheme = isLightTheme,
                    onClick = {
                        defaultTypeExpanded = false
                        viewModel.send(ExpenseAddIntent.SetDefaultType(null))
                    }
                )
                Expense.allExpenseTypes.forEach { type ->
                    DefaultTypeMenuItem(
                        title = type.title,
                        selected = state.value.defaultType == type,
                        isLightTheme = isLightTheme,
                        leadingIcon = { ExpenseTypeIcon(type) },
                        onClick = {
                            defaultTypeExpanded = false
                            viewModel.send(ExpenseAddIntent.SetDefaultType(type))
                        }
                    )
                }
            }
        }
        state.value.defaultType?.let { defaultType ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "По умолчанию: ${defaultType.title}",
                modifier = Modifier.fillMaxWidth(),
                color = grayColor(isLightTheme),
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        UiChooser(
            isLightTheme = isLightTheme,
            value = state.value.date?.let { formatLocalDate(it) } ?: "",
            onClick = { viewModel.send(ExpenseAddIntent.ChangeDatePickerVisible(true)) },
            modifier = Modifier.fillMaxWidth(),
            placeholderText = "Дата",
            leadingIcon = painterResource(R.drawable.my_car_calendar),
            leadingIconContentDescription = "Значок календаря"
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
        UiTextField(
            isLightTheme = isLightTheme,
            value = state.value.note?.toString() ?: "",
            onValueChange = { viewModel.send(ExpenseAddIntent.ChangeNote(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            placeholderText = "Примечание",
            leadingIcon = painterResource(R.drawable.my_car_comment),
            contentDescription = "Значок комментария",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
private fun ExpenseTypeIcon(type: ExpenseType) {
    Image(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        painter = getImageForExpense(type),
        contentDescription = "Значок типа траты ${type.title}"
    )
}

@Composable
private fun DefaultTypeMenuItem(
    title: String,
    selected: Boolean,
    isLightTheme: Boolean,
    onClick: () -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    DropdownMenuItem(
        text = { Text(text = title, fontSize = 16.sp) },
        onClick = onClick,
        leadingIcon = leadingIcon,
        trailingIcon = if (selected) {
            { Text(text = "✓", color = GreenColor, fontSize = 18.sp) }
        } else {
            null
        },
        colors = MenuDefaults.itemColors(
            textColor = grayColor(isLightTheme),
            leadingIconColor = grayColor(isLightTheme),
            trailingIconColor = GreenColor
        )
    )
}

@Composable
private fun ColumnScope.DownButton(
    isLightTheme: Boolean,
    keyboard: SoftwareKeyboardController?,
    viewModel: ExpenseAddViewModel,
    navHostController: NavHostController,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    state: State<ExpenseAddState>,
    carId: Long
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
                    ExpenseAddIntent.SaveClick(
                        onSuccess = navHostController::popBackStack,
                        onError = { msg ->
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar(
                                    message = msg,
                                    withDismissAction = true
                                )
                            }
                        },
                        carId = carId
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
