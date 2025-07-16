package com.andef.mycarandef.work.presentation.workadd

import java.time.LocalDate

data class WorkAddState(
    val workId: Long? = null,
    val workTitle: String = "",
    val mileage: Int? = null,
    val date: LocalDate? = null,
    val note: String? = null,
    val isLoading: Boolean = false,
    val datePickerVisible: Boolean = false,
    val saveButtonEnabled: Boolean = false,
    val isAdd: Boolean = true
)
