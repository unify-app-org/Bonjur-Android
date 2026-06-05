package com.bonjur.designSystem.components.radioSelect

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Capsule radio row. Mirrors iOS `RadioSelectItemView`.
 */
@Composable
fun RadioSelectItem(
    id: String,
    title: String,
    isSelected: Boolean,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .border(
                BorderStroke(
                    width = if (isSelected) 1.dp else 0.5.dp,
                    color = if (isSelected) Palette.black else Palette.graySecondary
                ),
                CircleShape
            )
            .clickable { onClick(id) }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .border(
                    1.dp,
                    if (isSelected) Palette.black else Palette.graySecondary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Palette.black, CircleShape)
                )
            }
        }

        Text(
            text = title,
            style = AppTypography.BodyTextMd.regular,
            color = if (isSelected) Palette.blackHigh else Palette.grayPrimary
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}
