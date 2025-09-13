package com.andef.mycarandef.car.di

import android.app.Application
import com.andef.mycarandef.data.MyCarDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CarDaoModule {
    @Provides
    @Singleton
    fun provideCarDao(application: Application) = MyCarDatabase.getInstance(application).carDao
}