package com.andef.mycarandef.work.di

import androidx.lifecycle.ViewModel
import com.andef.mycarandef.viewmodel.ViewModelKey
import com.andef.mycarandef.work.presentation.workadd.WorkAddViewModel
import com.andef.mycarandef.work.presentation.workmain.WorkMainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WorkMainViewModel::class)
    fun bindWorkMainViewModel(impl: WorkMainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WorkAddViewModel::class)
    fun bindWorkAddViewModel(impl: WorkAddViewModel): ViewModel
}