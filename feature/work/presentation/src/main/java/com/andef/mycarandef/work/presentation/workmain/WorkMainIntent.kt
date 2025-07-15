package com.andef.mycarandef.work.presentation.workmain

sealed class WorkMainIntent {
    data object SubscribeForWorks : WorkMainIntent()
}