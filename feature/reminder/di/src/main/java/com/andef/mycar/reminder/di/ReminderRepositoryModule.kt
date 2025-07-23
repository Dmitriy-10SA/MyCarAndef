package com.andef.mycar.reminder.di

import com.andef.mycar.reminder.data.repository.ReminderRepositoryImpl
import com.andef.mycar.reminder.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ReminderRepositoryModule {
    @Binds
    @Singleton
    fun bindReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}