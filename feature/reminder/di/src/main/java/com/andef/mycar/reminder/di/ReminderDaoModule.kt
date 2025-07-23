package com.andef.mycar.reminder.di

import android.app.Application
import com.andef.mycarandef.data.MyCarDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReminderDaoModule {
    @Provides
    @Singleton
    fun provideReminderDao(application: Application) =
        MyCarDatabase.getInstance(application).reminderDao
}