package com.andef.mycarandef.di

import android.app.Activity
import android.app.Application
import com.andef.mycarandef.di.shprefs.ShPrefsModule
import com.andef.mycarandef.start.di.StartRepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ShPrefsModule::class,
        StartRepositoryModule::class
    ]
)
interface MyCarComponent {
    fun inject(activity: Activity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): MyCarComponent
    }
}