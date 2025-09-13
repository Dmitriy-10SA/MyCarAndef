package com.andef.mycarandef.map.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.map.presentation.MapMainViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MapViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MapMainViewModel::class)
    fun bindMapMainViewModel(impl: MapMainViewModel): ViewModel
}