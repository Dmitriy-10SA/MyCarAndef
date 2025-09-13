package com.andef.mycar.reminder.di

import androidx.lifecycle.ViewModel
import com.andef.mycar.reminder.presentation.allreminders.AllRemindersViewModel
import com.andef.mycar.reminder.presentation.reminderadd.ReminderAddViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ReminderViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AllRemindersViewModel::class)
    fun bindAllRemindersViewModel(impl: AllRemindersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReminderAddViewModel::class)
    fun bindReminderAddViewModel(impl: ReminderAddViewModel): ViewModel
}