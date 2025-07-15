package com.andef.mycarandef.work.presentation.workmain

import java.time.LocalDate

sealed class WorkMainIntent {
    data class SubscribeForWorks(val currentCarId: Long) : WorkMainIntent()
    data class BottomSheetVisibleChange(
        val isVisible: Boolean,
        val workTitle: String? = null,
        val workDate: LocalDate? = null,
        val workId: Long? = null
    ) : WorkMainIntent()
}