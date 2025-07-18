package com.andef.mycarandef.common

import android.app.Activity
import android.app.Application
import com.andef.mycarandef.car.di.CarDaoModule
import com.andef.mycarandef.car.di.CarRepositoryModule
import com.andef.mycarandef.car.di.CarViewModelModule
import com.andef.mycarandef.car.domain.usecases.GetAllCarsUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarIdAsFlowUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarImageUriAsFlowUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarNameAsFlowUseCase
import com.andef.mycarandef.car.domain.usecases.GetCurrentCarNameUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarIdUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarImageUriUseCase
import com.andef.mycarandef.car.domain.usecases.SetCurrentCarNameUseCase
import com.andef.mycarandef.expense.di.ExpenseDaoModule
import com.andef.mycarandef.expense.di.ExpenseRepositoryModule
import com.andef.mycarandef.expense.di.ExpenseViewModelModule
import com.andef.mycarandef.map.di.MapRepositoryModule
import com.andef.mycarandef.start.di.StartRepositoryModule
import com.andef.mycarandef.start.di.StartViewModelModule
import com.andef.mycarandef.start.domain.usecases.GetIsFirstStartUseCase
import com.andef.mycarandef.uitheme.di.UiThemeRepositoryModule
import com.andef.mycarandef.uitheme.domain.usecases.GetIsLightThemeUseCase
import com.andef.mycarandef.viewmodel.ViewModelFactory
import com.andef.mycarandef.work.di.WorkDaoModule
import com.andef.mycarandef.work.di.WorkRepositoryModule
import com.andef.mycarandef.work.di.WorkViewModelModule
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
        CarViewModelModule::class,

        //Expense
        ExpenseRepositoryModule::class,
        ExpenseDaoModule::class,
        ExpenseViewModelModule::class,

        //Map
        MapRepositoryModule::class,

        //Work
        WorkRepositoryModule::class,
        WorkDaoModule::class,
        WorkViewModelModule::class,

        //UiTheme
        UiThemeRepositoryModule::class
    ]
)
interface MyCarComponent {
    fun inject(activity: Activity)

    val viewModelFactory: ViewModelFactory
    val getIsFirstStartUseCase: GetIsFirstStartUseCase
    val getIsLightThemeUseCase: GetIsLightThemeUseCase
    val getAllCarsUseCase: GetAllCarsUseCase
    val getCurrentCarIdUseCase: GetCurrentCarIdUseCase
    val getCurrentCarNameUseCase: GetCurrentCarNameUseCase
    val getCurrentCarImageUriUseCase: GetCurrentCarImageUriUseCase
    val getCurrentCarImageUriAsFlowUseCase: GetCurrentCarImageUriAsFlowUseCase
    val getCurrentCarIdAsFlowUseCase: GetCurrentCarIdAsFlowUseCase
    val getCurrentCarNameAsFlowUseCase: GetCurrentCarNameAsFlowUseCase
    val setCurrentCarIdUseCase: SetCurrentCarIdUseCase
    val setCurrentCarImageUriUseCase: SetCurrentCarImageUriUseCase
    val setCurrentCarNameUseCase: SetCurrentCarNameUseCase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): MyCarComponent
    }
}
