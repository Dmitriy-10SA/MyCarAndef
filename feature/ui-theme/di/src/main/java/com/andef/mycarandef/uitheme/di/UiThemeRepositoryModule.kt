package com.andef.mycarandef.uitheme.di

import com.andef.mycarandef.uitheme.data.repository.UiThemeRepositoryImpl
import com.andef.mycarandef.uitheme.domain.repository.UiThemeRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UiThemeRepositoryModule {
    @Binds
    @Singleton
    fun bindUiThemeRepository(impl: UiThemeRepositoryImpl): UiThemeRepository
}