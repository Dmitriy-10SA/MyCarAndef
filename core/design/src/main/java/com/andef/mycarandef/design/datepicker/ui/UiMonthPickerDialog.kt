package com.andef.mycarandef.design.datepicker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.auto.resize.text.ui.AutoResizeText
import com.andef.mycarandef.design.dialog.container.ui.UiDialogContainer
import com.andef.mycarandef.design.theme.GreenColor
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import java.time.LocalDate

@Composable
fun UiMonthPickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    initialYear: Int,
    initialMonth: Int,
    onDismissRequest: () -> Unit,
    onOkClick: (year: Int, month: Int) -> Unit
) {
    if (!isVisible) return

    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }

    UiDialogContainer(isLightTheme = isLightTheme, onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.widthIn(max = 360.dp)) {
            AutoResizeText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp),
                text = "${monthNames[selectedMonth - 1]} $selectedYear",
                color = blackOrWhiteColor(isLightTheme),
                maxFontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PickerNavigationButton(
                    text = "<",
                    enabled = selectedYear >= currentDate.minusYears(12).year,
                    isLightTheme = isLightTheme,
                    onClick = { selectedYear-- }
                )
                Text(
                    text = selectedYear.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = blackOrWhiteColor(isLightTheme)
                )
                PickerNavigationButton(
                    text = ">",
                    enabled = selectedYear <= currentDate.plusYears(12).year,
                    isLightTheme = isLightTheme,
                    onClick = { selectedYear++ }
                )
            }
            HorizontalDivider(color = grayColor(isLightTheme), thickness = 0.5.dp)
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                monthNames.chunked(3).forEachIndexed { rowIndex, months ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        months.forEachIndexed { index, monthName ->
                            val monthNumber = rowIndex * 3 + index + 1
                            PickerItem(
                                modifier = Modifier.weight(1f),
                                text = monthName,
                                selected = monthNumber == selectedMonth,
                                isCurrent = selectedYear == currentDate.year &&
                                        monthNumber == currentDate.monthValue,
                                isLightTheme = isLightTheme,
                                onClick = { selectedMonth = monthNumber }
                            )
                        }
                    }
                }
            }
            HorizontalDivider(color = grayColor(isLightTheme), thickness = 0.5.dp)
            Box(modifier = Modifier.fillMaxWidth().height(48.dp)) {
                PickerSaveButton(
                    modifier = Modifier.matchParentSize(),
                    isLightTheme = isLightTheme,
                    onClick = { onOkClick(selectedYear, selectedMonth) }
                )
            }
        }
    }
}

@Composable
internal fun PickerItem(
    modifier: Modifier,
    text: String,
    selected: Boolean,
    isCurrent: Boolean,
    isLightTheme: Boolean,
    onClick: () -> Unit
) {
    val indicator = if (isCurrent && !selected) {
        Modifier.drawBehind {
            drawCircle(
                color = GreenColor,
                radius = 2.5.dp.toPx(),
                center = Offset(size.width / 2f, size.height - 6.dp.toPx())
            )
        }
    } else Modifier

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) GreenColor else Color.Transparent)
            .clickable(onClick = onClick)
            .then(indicator)
            .padding(horizontal = 6.dp, vertical = 11.dp),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = if (selected) WhiteColor else blackOrWhiteColor(isLightTheme)
    )
}

@Composable
internal fun PickerNavigationButton(
    text: String,
    enabled: Boolean,
    isLightTheme: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        enabled = enabled,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(contentColor = blackOrWhiteColor(isLightTheme))
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) GreenColor else GreenColor.copy(alpha = 0.3f)
        )
    }
}

@Composable
internal fun PickerSaveButton(
    modifier: Modifier,
    isLightTheme: Boolean,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = blackOrWhiteColor(isLightTheme))
    ) {
        Text("Сохранить", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GreenColor)
    }
}

private val monthNames = listOf(
    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
)
