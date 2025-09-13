package com.andef.mycarandef.design.card.reminder.ui

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
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatLocalTimeToString
import java.time.LocalDate
import java.time.LocalTime

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
        shape = RoundedCornerShape(0.dp),
        colors = colors(isLightTheme = isLightTheme),
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
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = reminder.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = blackOrWhiteColor(isLightTheme)
                )
                Text(
                    text = if (reminder.date > LocalDate.now()) {
                        "Ожидается"
                    } else if (reminder.date == LocalDate.now() && reminder.time > LocalTime.now()) {
                        "Ожидается"
                    } else {
                        "Завершено"
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = grayColor(isLightTheme)
                )
            }
            Spacer(modifier = Modifier.width(3.dp))
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatLocalDate(reminder.date),
                    fontSize = 16.sp,
                    color = blackOrWhiteColor(isLightTheme)
                )
                Text(
                    text = formatLocalTimeToString(reminder.time),
                    fontSize = 16.sp,
                    color = grayColor(isLightTheme)
                )
            }
        }
    }
}

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    contentColor = blackOrWhiteColor(isLightTheme)
)