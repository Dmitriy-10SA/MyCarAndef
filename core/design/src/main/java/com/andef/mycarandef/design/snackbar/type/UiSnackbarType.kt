package com.andef.mycarandef.design.snackbar.type

import androidx.compose.ui.graphics.Color
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.RedColor

sealed class UiSnackbarType(val containerColor: Color) {
    data object Error : UiSnackbarType(containerColor = RedColor)
    data object Success : UiSnackbarType(containerColor = GreenColor)
}