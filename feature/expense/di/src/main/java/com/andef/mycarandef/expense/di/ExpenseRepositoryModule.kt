package com.andef.mycarandef.expense.di

import com.andef.mycarandef.expense.data.repository.ExpenseRepositoryImpl
import com.andef.mycarandef.expense.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ExpenseRepositoryModule {
    @Binds
    @Singleton
    fun bindExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository
}