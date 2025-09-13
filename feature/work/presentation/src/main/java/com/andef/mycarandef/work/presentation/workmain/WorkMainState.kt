package com.andef.mycarandef.work.presentation.workmain

import com.andef.mycarandef.work.domain.entities.Work
import java.time.LocalDate

data class WorkMainState(
    val works: List<Work> = listOf(),
    val showBottomSheet: Boolean = false,
    val mileageInBottomSheet: Int? = null,
    val workTitleInBottomSheet: String? = null,
    val workDateInBottomSheet: LocalDate? = null,
    val workIdInBottomSheet: Long? = null,
    val carIdForWorkBottomSheet: Long? = null,
    val deleteDialogVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val initialFirstVisibleItemIndex: Int = 0,
    val initialFirstVisibleItemScrollOffset: Int = 0
)