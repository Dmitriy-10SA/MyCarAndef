package com.andef.mycarandef.design.menu.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andef.mycarandef.design.R
import com.andef.mycarandef.design.theme.Black
import com.andef.mycarandef.design.theme.Blue
import com.andef.mycarandef.design.theme.DarkGray
import com.andef.mycarandef.design.theme.GrayForDark
import com.andef.mycarandef.design.theme.GrayForLight
import com.andef.mycarandef.design.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> UiMenu(
    modifier: Modifier = Modifier,
    items: List<T>,
    itemToString: (T) -> String,
    itemToLeadingIcon: @Composable ((T) -> Unit)? = null,
    isLightTheme: Boolean,
    value: String,
    placeholderText: String,
    textFieldLeadingIcon: Painter,
    textFieldLeadingIconContentDescription: String,
    onItemClick: (T) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = onExpandedChange) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(PrimaryNotEditable),
            value = value,
            onValueChange = {},
            placeholder = {
                Text(
                    text = placeholderText,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    painter = textFieldLeadingIcon,
                    contentDescription = textFieldLeadingIconContentDescription
                )
            },
            trailingIcon = {
                Icon(
                    painter = if (expanded) painterResource(R.drawable.arrow_drop_up)
                    else painterResource(R.drawable.arrow_drop_down),
                    contentDescription = "Открытие закрытие меню"
                )
            },
            singleLine = true,
            readOnly = true,
            shape = shape,
            colors = colors(value = value, isLightTheme = isLightTheme),
            textStyle = TextStyle(color = if (isLightTheme) Black else White, fontSize = 16.sp)
        )
        ExposedDropdownMenu(
            modifier = Modifier.border(
                width = 1.dp,
                color = if (isLightTheme) GrayForLight else GrayForDark,
                shape = shape
            ),
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            shape = shape,
            containerColor = if (isLightTheme) White else DarkGray
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = { onItemClick(item) },
                    text = {
                        Text(text = itemToString(item), fontSize = 16.sp)
                    },
                    leadingIcon = if (itemToLeadingIcon != null) {
                        {
                            itemToLeadingIcon(item)
                        }
                    } else {
                        null
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = if (isLightTheme) GrayForLight else GrayForDark,
                        leadingIconColor = if (isLightTheme) GrayForLight else GrayForDark
                    ),
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

private val shape = RoundedCornerShape(16.dp)

@Composable
private fun colors(value: String, isLightTheme: Boolean) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = if (isLightTheme) GrayForLight else GrayForDark,
    focusedContainerColor = if (isLightTheme) White else DarkGray,
    focusedLabelColor = if (isLightTheme) Black else White,
    focusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
    focusedLeadingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    focusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedTextColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedContainerColor = if (isLightTheme) White else DarkGray,
    unfocusedLabelColor = if (isLightTheme) Black else White,
    unfocusedPlaceholderColor = if (isLightTheme) GrayForLight else GrayForDark,
    unfocusedLeadingIconColor = when (value.isEmpty()) {
        true -> if (isLightTheme) GrayForLight else GrayForDark
        else -> if (isLightTheme) Black else White
    },
    cursorColor = if (isLightTheme) Black else White,
    unfocusedBorderColor = if (isLightTheme) GrayForLight else GrayForDark,
    selectionColors = TextSelectionColors(
        handleColor = Blue,
        backgroundColor = Blue.copy(alpha = 0.2f)
    )
)