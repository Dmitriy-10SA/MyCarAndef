package com.andef.mycarandef.start.di

import com.andef.mycarandef.start.data.repository.StartRepositoryImpl
import com.andef.mycarandef.start.domain.repository.StartRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface StartRepositoryModule {
    @Binds
    @Singleton
    fun bindStartRepository(impl: StartRepositoryImpl): StartRepository
}