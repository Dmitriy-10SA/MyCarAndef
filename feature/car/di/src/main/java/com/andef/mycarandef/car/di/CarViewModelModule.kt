package com.andef.mycarandef.car.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.car.presentation.caradd.CarAddViewModel
import com.andef.mycarandef.car.presentation.carmain.CarMainViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CarViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CarMainViewModel::class)
    fun bindCarMainViewModel(impl: CarMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CarAddViewModel::class)
    fun bindCarAddViewModel(impl: CarAddViewModel): ViewModel
}