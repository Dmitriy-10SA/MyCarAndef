package com.andef.mycarandef.start.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.start.presentation.TempViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface StartViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TempViewModel::class)
    fun bindTempViewModel(impl: TempViewModel): ViewModel
}