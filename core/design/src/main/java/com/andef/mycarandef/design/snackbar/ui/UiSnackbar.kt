package com.andef.mycarandef.design.snackbar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andef.mycarandef.design.snackbar.type.UiSnackbarType
import com.andef.mycarandef.design.theme.WhiteColor

@Composable
fun UiSnackbar(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    type: UiSnackbarType
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ) {
        SnackbarHost(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 6.dp, end = 6.dp),
            hostState = snackbarHostState,
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    shape = shape,
                    containerColor = type.containerColor,
                    contentColor = WhiteColor,
                    actionColor = WhiteColor,
                    actionContentColor = WhiteColor,
                    dismissActionContentColor = WhiteColor
                )
            }
        )
    }
}

private val shape = RoundedCornerShape(16.dp)