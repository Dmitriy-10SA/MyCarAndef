package com.andef.mycarandef.design.card.date.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.darkGrayOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import com.andef.mycarandef.utils.formatLocalDate
import com.andef.mycarandef.utils.formatPriceRuble
import java.time.LocalDate
import kotlin.math.abs

@Composable
fun UiDateAndAmountRow(
    modifier: Modifier = Modifier,
    isLightTheme: Boolean,
    date: LocalDate,
    amount: Double,
    isIncome: Boolean
) {
    val amountText = if (isIncome) {
        "+${formatPriceRuble(amount)}"
    } else {
        "-${formatPriceRuble(amount)}"
    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = cardColors(isLightTheme = isLightTheme)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                color = blackOrWhiteColor(isLightTheme),
                text = formatLocalDate(date),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                color = grayColor(isLightTheme),
                text = amountText,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun cardColors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = darkGrayOrWhiteColor(isLightTheme),
    contentColor = blackOrWhiteColor(isLightTheme)
)

@Composable
fun UiDateAndAmountRow(
    modifier: Modifier = Modifier,
    isLightTheme: Boolean,
    startDate: LocalDate,
    isIncome: Boolean,
    endDate: LocalDate,
    totalAmount: Double
) {
    val dates = if (startDate == endDate) {
        formatLocalDate(startDate)
    } else {
        "${formatLocalDate(startDate)} - ${formatLocalDate(endDate)}"
    }
    val sign = when (totalAmount != 0.0) {
        true -> if (isIncome) "+" else "-"
        false -> ""
    }
    Card(
        modifier = modifier.shadow(
            elevation = if (isLightTheme) 8.dp else 3.dp,
            spotColor = blackOrWhiteColor(isLightTheme),
            shape = RoundedCornerShape(16.dp),
            clip = true
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = (grayColor(isLightTheme)).copy(alpha = 0.3f)
        ),
        colors = cardColors(isLightTheme = isLightTheme)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                color = grayColor(isLightTheme),
                text = dates,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
            Text(
                color = blackOrWhiteColor(isLightTheme),
                text = "Итого: $sign${formatPriceRuble(abs(totalAmount))}",
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}