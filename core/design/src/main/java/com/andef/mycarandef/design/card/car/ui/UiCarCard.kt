package com.andef.mycarandef.design.card.car.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White

@Composable
fun UiCarCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    car: Car
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = cardColors(),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLightTheme) {
                GrayForLight.copy(alpha = 0.3f)
            } else {
                GrayForDark.copy(alpha = 0.3f)
            }
        )
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.car_wo_photo),
                contentDescription = "Car Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(180.dp)
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                text = "${car.brand} ${car.model}",
                color = White,
                fontSize = 18.sp,
                style = TextStyle(
                    shadow = Shadow(color = Black, offset = Offset(0f, 0f), blurRadius = 5f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun cardColors() = CardDefaults.cardColors(
    containerColor = Color.Transparent,
    contentColor = White
)