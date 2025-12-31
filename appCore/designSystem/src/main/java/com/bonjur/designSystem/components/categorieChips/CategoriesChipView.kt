package com.bonjur.designSystem.components.categorieChips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette

@Composable
fun CategoriesChipsView(
    model: CategoriesChipModel,
    onClick: (Int) -> Unit
) {
    Text(
        text = model.title,
        color = Palette.blackHigh,
        modifier = Modifier
            .background(model.backgroundColor, CircleShape)
            .border(
                BorderStroke(0.5.dp, model.borderColor),
                CircleShape
            )
            .clip(CircleShape)
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(model.id)
            }
    )
}
