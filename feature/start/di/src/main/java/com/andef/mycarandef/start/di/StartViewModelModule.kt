package com.andef.mycarandef.start.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.start.presentation.carinput.CarInputViewModel
import com.andef.mycarandef.start.presentation.usernameinput.UsernameInputViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface StartViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UsernameInputViewModel::class)
    fun bindUsernameInputViewModel(impl: UsernameInputViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CarInputViewModel::class)
    fun bindCarInputViewModel(impl: CarInputViewModel): ViewModel
}