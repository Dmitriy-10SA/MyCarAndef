package com.andef.mycarandef.design.card.car.ui

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.BlackColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.grayColor

@Composable
fun UiCarCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    car: Car,
    context: Context
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = cardColors(),
        border = BorderStroke(
            width = 1.dp,
            color = grayColor(isLightTheme).copy(alpha = 0.3f)
        )
    ) {
        Box {
            CarPhoto(car = car, context = context)
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
                text = "${car.brand} ${car.model}",
                color = WhiteColor,
                fontSize = 18.sp,
                style = TextStyle(
                    shadow = Shadow(color = BlackColor, offset = Offset(0f, 0f), blurRadius = 5f)
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BoxScope.CarPhoto(car: Car, context: Context) {
    if (!car.photo.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(car.photo)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.my_car_car_wo_photo),
            error = painterResource(R.drawable.my_car_car_wo_photo),
            modifier = Modifier.height(160.dp),
            contentScale = ContentScale.Crop,
            contentDescription = "Фото машины"
        )
    } else {
        Image(
            painter = painterResource(R.drawable.my_car_car_wo_photo),
            contentDescription = "Фото машины из гаража без фото",
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(160.dp)
        )
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun cardColors() = CardDefaults.cardColors(
    containerColor = Color.Transparent,
    contentColor = White
)