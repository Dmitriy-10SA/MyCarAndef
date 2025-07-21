package com.andef.mycarandef.expense.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.expense.presentation.expenseadd.ExpenseAddViewModel
import com.andef.mycarandef.expense.presentation.expenseanalysis.ExpenseAnalysisViewModel
import com.andef.mycarandef.expense.presentation.expensemain.ExpenseMainViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExpenseViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ExpenseMainViewModel::class)
    fun bindExpenseMainViewModel(impl: ExpenseMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseAddViewModel::class)
    fun bindExpenseAddViewModel(impl: ExpenseAddViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseAnalysisViewModel::class)
    fun bindExpenseAnalysisViewModel(impl: ExpenseAnalysisViewModel): ViewModel
}