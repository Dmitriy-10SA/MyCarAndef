package com.andef.mycarandef.di

import android.app.Activity
import android.app.Application
import com.andef.mycarandef.car.di.CarDaoModule
import com.andef.mycarandef.car.di.CarRepositoryModule
import com.andef.mycarandef.expense.di.ExpenseDaoModule
import com.andef.mycarandef.expense.di.ExpenseRepositoryModule
import com.andef.mycarandef.map.di.MapRepositoryModule
import com.andef.mycarandef.start.di.ShPrefsModule
import com.andef.mycarandef.start.di.StartRepositoryModule
import com.andef.mycarandef.work.di.WorkDaoModule
import com.andef.mycarandef.work.di.WorkRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ShPrefsModule::class,
        StartRepositoryModule::class,
        CarRepositoryModule::class,
        CarDaoModule::class,
        ExpenseRepositoryModule::class,
        ExpenseDaoModule::class,
        MapRepositoryModule::class,
        WorkRepositoryModule::class,
        WorkDaoModule::class
    ]
)
interface MyCarComponent {
    fun inject(activity: Activity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): MyCarComponent
    }
}