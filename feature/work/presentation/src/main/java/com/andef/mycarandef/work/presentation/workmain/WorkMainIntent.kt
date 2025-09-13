package com.andef.mycarandef.work.presentation.workmain

import java.time.LocalDate

sealed class WorkMainIntent {
    data class SubscribeForWorks(val currentCarId: Long) : WorkMainIntent()
    data class BottomSheetVisibleChange(
        val isVisible: Boolean,
        val workMileage: Int? = null,
        val workTitle: String? = null,
        val workDate: LocalDate? = null,
        val workId: Long? = null,
        val carId: Long? = null
    ) : WorkMainIntent()

    data class SaveScrollState(
        val initialFirstVisibleItemIndex: Int,
        val initialFirstVisibleItemScrollOffset: Int
    ) : WorkMainIntent()

    data class DeleteWork(val workId: Long, val onError: (String) -> Unit) : WorkMainIntent()
    data class ChangeDeleteDialogVisible(val isVisible: Boolean) : WorkMainIntent()
}