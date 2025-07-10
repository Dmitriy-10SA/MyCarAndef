package com.andef.mycarandef.common

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShPrefsModule {
    @Provides
    @Singleton
    fun provideShPrefs(application: Application): SharedPreferences {
        return application.getSharedPreferences(SH_PREFS, Context.MODE_PRIVATE)
    }

    companion object {
        private const val SH_PREFS = "sh-prefs"
    }
}