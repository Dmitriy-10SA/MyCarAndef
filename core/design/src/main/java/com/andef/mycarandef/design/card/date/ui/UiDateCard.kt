package com.andef.mycarandef.design.card.date.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.utils.formatLocalDate
import java.time.LocalDate

@Composable
fun UiDateCard(
    modifier: Modifier = Modifier,
    isLightTheme: Boolean,
    date: LocalDate
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = colors(isLightTheme = isLightTheme),
        border = BorderStroke(
            width = 1.dp,
            color = grayColor(isLightTheme).copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = formatLocalDate(date),
                fontSize = 14.sp,
                color = grayColor(isLightTheme)
            )
        }
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    contentColor = blackOrWhiteColor(isLightTheme)
)