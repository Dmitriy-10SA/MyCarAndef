package com.andef.mycarandef.work.presentation.workmain

import com.andef.mycarandef.work.domain.entities.Work

data class WorkMainState(
    val works: List<Work> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)