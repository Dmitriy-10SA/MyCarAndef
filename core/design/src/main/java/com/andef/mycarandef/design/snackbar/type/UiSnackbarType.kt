package com.andef.mycarandef.design.snackbar.type

import androidx.compose.ui.graphics.Color
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.Red

sealed class UiSnackbarType(val containerColor: Color) {
    data object Error : UiSnackbarType(containerColor = Red)
    data object Success : UiSnackbarType(containerColor = Blue)
}