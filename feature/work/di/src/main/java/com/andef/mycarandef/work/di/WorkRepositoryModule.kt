package com.andef.mycarandef.work.di

import com.andef.mycarandef.work.data.repository.WorkRepositoryImpl
import com.andef.mycarandef.work.domain.repository.WorkRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface WorkRepositoryModule {
    @Binds
    @Singleton
    fun bindWorkRepository(impl: WorkRepositoryImpl): WorkRepository
}