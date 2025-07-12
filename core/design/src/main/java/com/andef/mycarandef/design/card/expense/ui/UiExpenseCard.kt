package com.andef.mycarandef.design.card.expense.ui

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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White
import com.andef.mycarandef.expense.domain.entities.Expense
import com.andef.mycarandef.expense.domain.entities.ExpenseType
import com.andef.mycarandef.utils.formatPriceRuble

@Composable
fun UiExpenseCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isLightTheme: Boolean,
    expense: Expense
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
                modifier = Modifier.size(45.dp).clip(CircleShape),
                painter = getImageForExpense(expense.type),
                contentDescription = "Фото для категории траты"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = expense.type.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = if (isLightTheme) Black else White
                )
                Text(
                    text = expense.note ?: "Примечания нет",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    color = if (isLightTheme) GrayForLight else GrayForDark
                )
            }
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = formatPriceRuble(expense.amount),
                fontSize = 16.sp,
                color = if (isLightTheme) Black else White
            )
        }
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun getImageForExpense(type: ExpenseType): Painter {
    return painterResource(
        when (type) {
            ExpenseType.FUEL -> R.drawable.fuel_photo
            ExpenseType.WORKS -> R.drawable.works_photo
            ExpenseType.WASHING -> R.drawable.washing_photo
            ExpenseType.OTHER -> R.drawable.other_photo
        }
    )
}

@Composable
private fun colors(isLightTheme: Boolean) = CardDefaults.cardColors(
    containerColor = if (isLightTheme) White else DarkGray,
    contentColor = if (isLightTheme) Black else White
)