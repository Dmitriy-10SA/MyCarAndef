package com.andef.mycarandef.work.di

import android.app.Application
import com.andef.mycarandef.data.MyCarDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WorkDaoModule {
    @Provides
    @Singleton
    fun provideWorkDao(application: Application) = MyCarDatabase.getInstance(application).workDao
}