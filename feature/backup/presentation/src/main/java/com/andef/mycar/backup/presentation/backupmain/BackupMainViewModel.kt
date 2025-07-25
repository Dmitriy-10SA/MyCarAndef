package com.andef.mycar.backup.presentation.backupmain

import androidx.lifecycle.ViewModel
import com.andef.mycar.reminder.domain.usecases.AddReminderUseCase
import com.andef.mycar.reminder.domain.usecases.GetAllRemindersAsListUseCase
import com.andef.mycarandef.car.domain.usecases.AddCarUseCase
import com.andef.mycarandef.car.domain.usecases.GetAllCarsAsListUseCase
import com.andef.mycarandef.expense.domain.usecases.AddExpenseUseCase
import com.andef.mycarandef.expense.domain.usecases.GetAllExpensesAsListUseCase
import com.andef.mycarandef.start.domain.usecases.GetUsernameUseCase
import com.andef.mycarandef.start.domain.usecases.SetIsFirstStartUseCase
import com.andef.mycarandef.start.domain.usecases.SetUsernameUseCase
import com.andef.mycarandef.uitheme.domain.usecases.GetIsLightThemeUseCase
import com.andef.mycarandef.work.domain.usecases.AddWorkUseCase
import com.andef.mycarandef.work.domain.usecases.GetAllWorksAsListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BackupMainViewModel @Inject constructor(
    private val getAllCarsAsListUseCase: GetAllCarsAsListUseCase,
    private val getAllWorksAsListUseCase: GetAllWorksAsListUseCase,
    private val getAllExpensesAsListUseCase: GetAllExpensesAsListUseCase,
    private val getAllRemindersAsListUseCase: GetAllRemindersAsListUseCase,
    private val getUsernameUseCase: GetUsernameUseCase,
    private val getIsLightThemeUseCase: GetIsLightThemeUseCase,
    private val addCarUseCase: AddCarUseCase,
    private val addWorkUseCase: AddWorkUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val addReminderUseCase: AddReminderUseCase,
    private val setUsernameUseCase: SetUsernameUseCase,
    private val setIsLightThemeUseCase: GetIsLightThemeUseCase,
    private val setIsFirstStartUseCase: SetIsFirstStartUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(BackupMainState())
    val state: StateFlow<BackupMainState> = _state

    fun send(intent: BackupMainIntent) {

    }
}