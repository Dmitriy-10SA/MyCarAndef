package com.andef.mycarandef.work.presentation.workmain

sealed class WorkMainIntent {
    data class SubscribeForWorks(val currentCarId: Long) : WorkMainIntent()
}