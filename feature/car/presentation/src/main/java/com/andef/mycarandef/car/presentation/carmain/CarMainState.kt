package com.andef.mycarandef.car.presentation.carmain

import com.andef.mycarandef.car.domain.entities.Car

data class CarMainState(
    val cars: List<Car> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val showBottomSheet: Boolean = false,
    val brandInBottomSheet: String? = null,
    val modelInBottomSheet: String? = null,
    val yearInBottomSheet: Int? = null,
    val imageUriInBottomSheet: String? = null,
    val registrationMarkOnBottomSheet: String? = null,
    val carIdInBottomSheet: Long? = null,
    val deleteDialogVisible: Boolean = false,
    val initialFirstVisibleItemIndex: Int = 0,
    val initialFirstVisibleItemScrollOffset: Int = 0
)
