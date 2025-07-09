package com.andef.mycarandef.expense.di

import android.app.Application
import com.andef.mycarandef.data.MyCarDatabase
import com.andef.mycarandef.expense.data.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ExpenseDaoModule {
    @Provides
    @Singleton
    fun provideExpenseDao(application: Application): ExpenseDao {
        return MyCarDatabase.getInstance(application).expenseDao
    }
}