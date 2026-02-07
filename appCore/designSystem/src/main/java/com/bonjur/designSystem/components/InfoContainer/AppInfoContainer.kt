package com.bonjur.designSystem.components.InfoContainer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun AppInfoContainer(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    spacing: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Palette.grayTeritary.copy(alpha = 0.18f),
                spotColor = Palette.grayTeritary.copy(alpha = 0.18f)
            )
            .background(
                color = Palette.white,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 0.5.dp,
                color = Palette.grayTeritary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = alignment,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(spacing)
    ) {
        content()
    }
}