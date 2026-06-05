package com.bonjur.designSystem.components.datePicker

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

/**
 * Read-only capsule field that opens a date picker on tap. Mirrors iOS `DatePickerTextField`.
 * The actual picker dialog is owned by the caller via [onTap].
 */
@Composable
fun DatePickerTextField(
    text: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Birthday",
    placeholder: String = "MM/DD/YYYY"
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = AppTypography.HeadingMd.medium,
            color = Palette.black,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable { onTap() }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text.ifEmpty { placeholder },
                style = AppTypography.BodyTextMd.regular,
                color = if (text.isEmpty()) Palette.grayPrimary else Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = Images.Icons.calendar(),
                contentDescription = null,
                tint = Palette.blackHigh
            )
        }
    }
}
