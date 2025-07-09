package com.andef.mycarandef.common

import android.app.Activity
import android.app.Application
import com.andef.mycarandef.car.di.CarDaoModule
import com.andef.mycarandef.car.di.CarRepositoryModule
import com.andef.mycarandef.expense.di.ExpenseDaoModule
import com.andef.mycarandef.expense.di.ExpenseRepositoryModule
import com.andef.mycarandef.map.di.MapRepositoryModule
import com.andef.mycarandef.start.di.StartRepositoryModule
import com.andef.mycarandef.start.di.StartViewModelModule
import com.andef.mycarandef.start.domain.usecases.GetIsFirstStartUseCase
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.andef.mycarandef.work.di.WorkDaoModule
import com.andef.mycarandef.work.di.WorkRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        //ShPrefs
        ShPrefsModule::class,

        //Start
        StartRepositoryModule::class,
        StartViewModelModule::class,

        //Car
        CarRepositoryModule::class,
        CarDaoModule::class,

        //Expense
        ExpenseRepositoryModule::class,
        ExpenseDaoModule::class,

        //Map
        MapRepositoryModule::class,

        //Work
        WorkRepositoryModule::class,
        WorkDaoModule::class
    ]
)
interface MyCarComponent {
    fun inject(activity: Activity)

    val viewModelFactory: ViewModelFactory
    val getIsFirstStartUseCase: GetIsFirstStartUseCase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): MyCarComponent
    }
}
