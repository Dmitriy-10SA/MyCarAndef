package com.andef.mycar.backup.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.bottomsheet.ui.UiModalBottomSheet
import com.andef.mycarandef.design.theme.WhiteColor
import com.andef.mycarandef.design.theme.blackOrWhiteColor
import com.andef.mycarandef.design.theme.grayColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.DownText(
    isLightTheme: Boolean,
    context: Context,
    feedbackSheetVisible: MutableState<Boolean>,
    feedbackSheetState: SheetState
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Нужна помощь?",
            color = grayColor(isLightTheme),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { feedbackSheetVisible.value = true })
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
    UiModalBottomSheet(
        isLightTheme = isLightTheme,
        isVisible = feedbackSheetVisible.value,
        onDismissRequest = { feedbackSheetVisible.value = false },
        sheetState = feedbackSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Нужна помощь? Напишите разработчику:",
                color = grayColor(isLightTheme),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(0.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(R.drawable.my_car_telegram_icon),
                    contentDescription = "Иконка телеграмм",
                    text = "Telegram",
                    onClick = {
                        Intent(Intent.ACTION_VIEW, "https://t.me/dsemkin".toUri()).apply {
                            context.startActivity(this)
                        }
                    }
                )
                AppItem(
                    isLightTheme = isLightTheme,
                    icon = painterResource(R.drawable.my_car_mail_icon),
                    contentDescription = "Иконка почты",
                    text = "Mail",
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:semkin_dmitriy10@vk.com".toUri()
                        }
                        context.startActivity(
                            Intent.createChooser(
                                emailIntent,
                                "Выберите почтовый клиент"
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.AppItem(
    isLightTheme: Boolean,
    icon: Painter,
    contentDescription: String,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .size(65.dp)
                .background(color = WhiteColor, shape = RoundedCornerShape(16.dp))
                .border(
                    width = 1.dp,
                    color = grayColor(isLightTheme).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(all = 7.dp),
            painter = icon,
            contentDescription = contentDescription
        )
        Text(
            text = text,
            color = blackOrWhiteColor(isLightTheme),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}