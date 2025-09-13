package com.andef.mycarandef.design.card.car.ui

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andef.mycarandef.car.domain.entities.Car
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor

@Composable
fun UiCarInBottomSheetCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    isCurrentCar: Boolean,
    car: Car,
    context: Context
) {
    val borderColor = if (isCurrentCar) {
        GreenColor
    } else {
        grayColor(isLightTheme).copy(alpha = 0.3f)
    }
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = cardColors(isLightTheme = isLightTheme),
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            CarPhoto(car = car, context = context)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                text = "${car.brand} ${car.model}",
                fontSize = 16.sp,
                color = blackOrWhiteColor(isLightTheme)
            )
        }
    }
}

@Composable
private fun RowScope.CarPhoto(car: Car, context: Context) {
    if (!car.photo.isNullOrBlank()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(car.photo)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.my_car_car_wo_photo),
            error = painterResource(R.drawable.my_car_car_wo_photo),
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "Фото машины"
        )
    } else {
        Image(
            painter = painterResource(R.drawable.my_car_car_wo_photo),
            contentDescription = "Фото машины из гаража без фото",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
        )
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun cardColors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    contentColor = blackOrWhiteColor(isLightTheme)
)