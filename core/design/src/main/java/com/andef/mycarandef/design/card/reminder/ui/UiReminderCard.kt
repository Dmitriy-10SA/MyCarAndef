package com.andef.mycarandef.design.card.reminder.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycar.reminder.domain.entities.Reminder
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatLocalTimeToString

@Composable
fun UiReminderCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    reminder: Reminder
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        colors = colors(isLightTheme = isLightTheme),
        border = BorderStroke(
            width = 1.dp,
            color = if (isLightTheme) {
                GrayForLight.copy(alpha = 0.3f)
            } else {
                GrayForDark.copy(alpha = 0.3f)
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(45.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.my_car_reminder_photo),
                contentScale = ContentScale.Crop,
                contentDescription = "Фото для напоминаний"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = reminder.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                color = if (isLightTheme) Black else White
            )
            Spacer(modifier = Modifier.width(3.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatLocalDate(reminder.date),
                    fontSize = 16.sp,
                    color = if (isLightTheme) Black else White
                )
                Text(
                    text = formatLocalTimeToString(reminder.time),
                    fontSize = 16.sp,
                    color = if (isLightTheme) GrayForLight else GrayForDark
                )
            }
        }
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = if (isLightTheme) White else DarkGray,
    contentColor = if (isLightTheme) Black else White
)