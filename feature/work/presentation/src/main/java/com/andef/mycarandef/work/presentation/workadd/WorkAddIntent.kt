package com.andef.mycarandef.work.presentation.workadd

import java.time.LocalDate

sealed class WorkAddIntent {
    data class ChangeWorkTitle(val workTitle: String) : WorkAddIntent()
    data class ChangeMileage(val mileage: Int?) : WorkAddIntent()
    data class ChangeNote(val note: String?) : WorkAddIntent()
    data class ChangeDate(val date: LocalDate) : WorkAddIntent()
    data class ChangeDatePickerVisible(val isVisible: Boolean) : WorkAddIntent()
    data class SaveClick(
        val carId: Long,
        val onSuccess: () -> Unit,
        val onError: (String) -> Unit
    ) : WorkAddIntent()

    data class InitWorkByLateWork(val workId: Long, val onError: (String) -> Unit) : WorkAddIntent()
}