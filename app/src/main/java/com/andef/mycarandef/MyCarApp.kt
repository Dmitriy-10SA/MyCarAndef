package com.andef.mycarandef

import android.app.Application
import com.andef.mycarandef.common.DaggerMyCarComponent

class MyCarApp : Application() {
    val component by lazy { DaggerMyCarComponent.factory().create(this) }
}