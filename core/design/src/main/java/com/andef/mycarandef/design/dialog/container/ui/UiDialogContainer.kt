package com.andef.mycarandef.design.dialog.container.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import kotlinx.coroutines.delay

private enum class DialogState { Ready, Opening, Opened, Closing, Closed }

@Composable
fun UiDialogContainer(
    isLightTheme: Boolean,
    onDismissRequest: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    var dialogState by remember { mutableStateOf(DialogState.Ready) }

    val scaleX by animateFloatAsState(
        targetValue = targetValue(dialogState),
        animationSpec = anim(dialogState)
    )

    val scaleY by animateFloatAsState(
        targetValue = targetValue(dialogState),
        animationSpec = anim(dialogState)
    )

    val alpha by animateFloatAsState(
        targetValue = targetValue(dialogState),
        animationSpec = anim(
            dialogState = dialogState,
            openingDuration = 250,
            openingEasing = LinearOutSlowInEasing,
            closingDuration = 150,
            closingEasing = FastOutLinearInEasing
        )
    )

    LaunchedEffect(dialogState) {
        if (dialogState == DialogState.Ready) {
            dialogState = DialogState.Opening
        } else if (dialogState == DialogState.Closing) {
            delay(300)
            dialogState = DialogState.Closed
        } else if (dialogState == DialogState.Closed) {
            onDismissRequest?.invoke()
        }
    }

    MainContent(
        onDismissRequest = {
            onDismissRequest?.let {
                dialogState = DialogState.Closing
            }
        },
        onScaleXEqual1 = { dialogState = DialogState.Opened },
        isLightTheme = isLightTheme,
        content = content,
        scaleX = scaleX,
        scaleY = scaleY,
        alpha = alpha
    )
}

@Composable
private fun MainContent(
    onDismissRequest: () -> Unit,
    onScaleXEqual1: () -> Unit,
    scaleX: Float,
    scaleY: Float,
    alpha: Float,
    content: @Composable () -> Unit,
    isLightTheme: Boolean
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.scaleX = scaleX
                    this.scaleY = scaleY
                    this.alpha = alpha

                    if (scaleX == 1f) {
                        onScaleXEqual1()
                    }
                }
                .noRippleClickable { onDismissRequest() }
        ) {
            Surface(
                modifier = Modifier
                    .padding(horizontal = 26.dp, vertical = 26.dp)
                    .wrapContentSize()
                    .animateContentSize()
                    .noRippleClickable {},
                shape = RoundedCornerShape(16.dp),
                color = darkGrayOrWhiteColor(isLightTheme),
            ) {
                content()
            }
        }
    }
}

@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    return this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}

private fun anim(
    dialogState: DialogState,
    openingDuration: Int = 300,
    openingEasing: Easing = FastOutSlowInEasing,
    closingDuration: Int = 200,
    closingEasing: Easing = FastOutLinearInEasing
): TweenSpec<Float> = when (dialogState) {
    DialogState.Opening -> tween(durationMillis = openingDuration, easing = openingEasing)
    DialogState.Closing -> tween(durationMillis = closingDuration, easing = closingEasing)
    else -> tween(0)
}

private fun targetValue(dialogState: DialogState) = when (dialogState) {
    DialogState.Ready -> 0f
    DialogState.Opening -> 1f
    DialogState.Opened -> 1f
    DialogState.Closing -> 0f
    DialogState.Closed -> 0f
}