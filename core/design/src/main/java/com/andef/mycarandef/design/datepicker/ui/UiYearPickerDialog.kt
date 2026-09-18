package com.andef.mycarandef.design.datepicker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.auto.resize.text.ui.AutoResizeText
import com.andef.mycarandef.design.dialog.container.ui.UiDialogContainer
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor
import java.time.LocalDate

@Composable
fun UiYearPickerDialog(
    isVisible: Boolean,
    isLightTheme: Boolean,
    initialYear: Int,
    onDismissRequest: () -> Unit,
    onOkClick: (year: Int) -> Unit
) {
    if (!isVisible) return

    val currentYear = LocalDate.now().year
    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var firstYear by remember { mutableIntStateOf(initialYear - initialYear.mod(12)) }

    UiDialogContainer(isLightTheme = isLightTheme, onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.widthIn(max = 360.dp)) {
            AutoResizeText(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = selectedYear.toString(),
                color = blackOrWhiteColor(isLightTheme),
                maxFontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PickerNavigationButton(
                    text = "<",
                    enabled = firstYear >= LocalDate.now().minusYears(12).year,
                    isLightTheme = isLightTheme,
                    onClick = { firstYear -= 12 }
                )
                Text(
                    text = "$firstYear - ${firstYear + 11}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = blackOrWhiteColor(isLightTheme)
                )
                PickerNavigationButton(
                    text = ">",
                    enabled = firstYear <= LocalDate.now().plusYears(12).year,
                    isLightTheme = isLightTheme,
                    onClick = { firstYear += 12 }
                )
            }
            HorizontalDivider(color = grayColor(isLightTheme), thickness = 0.5.dp)
            Column(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (firstYear..firstYear + 11).chunked(3).forEach { years ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        years.forEach { year ->
                            PickerItem(
                                modifier = Modifier.weight(1f),
                                text = year.toString(),
                                selected = year == selectedYear,
                                isCurrent = year == currentYear,
                                isLightTheme = isLightTheme,
                                onClick = { selectedYear = year }
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
                    onClick = { onOkClick(selectedYear) }
                )
            }
        }
    }
}
