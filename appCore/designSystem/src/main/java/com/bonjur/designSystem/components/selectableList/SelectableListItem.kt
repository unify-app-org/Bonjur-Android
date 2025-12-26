package com.bonjur.designSystem.components.selectableList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

// Composable
@Composable
fun SelectableListItem(
    model: SelectableListItemModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = model.backgroundColor,
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                width = model.borderWidth,
                color = model.borderColor,
                shape = RoundedCornerShape(100.dp)
            )
            .padding(horizontal = 25.dp, vertical = 16.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .then(
                Modifier
                    .padding(0.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(
            text = model.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )

        if (model.style == SelectableListItemModel.Style.MultiSelect) {
            Image(
                painter = if (model.selected) Images.Icons.selectedCheckBox()
                else Images.Icons.notSelectedCheckBox(),
                contentDescription = null
            )
        }
    }
}
