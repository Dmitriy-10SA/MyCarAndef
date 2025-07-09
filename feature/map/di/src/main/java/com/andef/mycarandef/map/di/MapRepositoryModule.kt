package com.andef.mycarandef.map.di

import com.andef.mycarandef.map.data.repository.MapRepositoryImpl
import com.andef.mycarandef.map.domain.repository.MapRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface MapRepositoryModule {
    @Binds
    @Singleton
    fun bindMapRepository(impl: MapRepositoryImpl): MapRepository
}