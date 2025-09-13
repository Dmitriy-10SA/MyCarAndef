package com.andef.mycarandef.design.card.work.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatMileage
import com.andef.mycarandef.work.domain.entities.Work

@Composable
fun UiWorkCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    isFirst: Boolean,
    work: Work
) {
    Column {
        if (!isFirst) {
            HorizontalDivider(
                modifier = modifier.padding(horizontal = 8.dp),
                thickness = 0.5.dp,
                color = (grayColor(isLightTheme)).copy(alpha = 0.5f)
            )
        }
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
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = work.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        color = blackOrWhiteColor(isLightTheme)
                    )
                    Text(
                        text = buildString {
                            val note = work.note
                            if (note.isNullOrBlank()) {
                                append("Примечания нет")
                            } else {
                                append(note)
                            }
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        color = grayColor(isLightTheme)
                    )
                }
                Spacer(modifier = Modifier.width(3.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = formatMileage(work.mileage),
                        fontSize = 16.sp,
                        color = blackOrWhiteColor(isLightTheme)
                    )
                    Text(
                        text = formatLocalDate(work.date),
                        fontSize = 14.sp,
                        color = grayColor(isLightTheme)
                    )
                }
            }
        }
    }
}

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    contentColor = blackOrWhiteColor(isLightTheme)
)