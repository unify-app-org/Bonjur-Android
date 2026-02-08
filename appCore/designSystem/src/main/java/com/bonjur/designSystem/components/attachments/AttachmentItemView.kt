package com.bonjur.designSystem.components.attachments

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

@Composable
fun AttachmentItemView(
    model: AttachmentItemModel,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
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
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // File icon
        Icon(
            painter = model.image(),
            contentDescription = null,
            tint = Palette.blackMedium,
            modifier = Modifier.size(24.dp)
        )
        
        // File info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = model.name,
                style = AppTypography.BodyTextSm.medium,
                color = Palette.blackHigh
            )
            
            Text(
                text = model.size,
                style = AppTypography.TextMd.regular,
                color = Palette.blackMedium
            )
        }
        
        // Delete button (if editable)
        if (model.canEdit) {
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = Images.Icons.trash(),
                    contentDescription = "Delete",
                    tint = Palette.blackMedium,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}