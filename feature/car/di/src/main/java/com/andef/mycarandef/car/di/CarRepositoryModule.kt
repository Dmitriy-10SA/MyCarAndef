package com.andef.mycarandef.car.di

import com.andef.mycarandef.car.data.repository.CarRepositoryImpl
import com.andef.mycarandef.car.domain.repository.CarRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface CarRepositoryModule {
    @Binds
    @Singleton
    fun bindCarRepository(impl: CarRepositoryImpl): CarRepository
}