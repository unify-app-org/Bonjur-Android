package com.bonjur.designSystem.components.links

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.components.fieldSchema.AppFieldSchema
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

/**
 * Editable list of links with an add button. Kotlin equivalent of iOS `AppLinksField`.
 */
@Composable
fun AppLinksField(
    links: List<AppFieldSchema.LinkItem>,
    onChange: (List<AppFieldSchema.LinkItem>) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Add link",
    addTitle: String = "Add link",
    maxCount: Int = 4
) {
    var sheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = title, style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)

        links.forEach { link ->
            LinkCell(
                link = link,
                onRemove = { onChange(links.filterNot { it.id == link.id }) }
            )
        }

        if (links.size < maxCount) {
            AddButton(addTitle = addTitle, onClick = { sheetOpen = true })
        }
    }

    if (sheetOpen) {
        AppBottomSheet(onDismiss = { sheetOpen = false }, showDragHandle = false) {
            AddLinkView(
                onAdd = { link ->
                    onChange(links + link)
                    sheetOpen = false
                },
                onClose = { sheetOpen = false }
            )
        }
    }
}

@Composable
private fun LinkCell(
    link: AppFieldSchema.LinkItem,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Palette.grayQuaternary)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = link.name,
                style = AppTypography.BodyTextSm.regular,
                color = Palette.blackMedium
            )
            Text(
                text = link.url,
                style = AppTypography.BodyTextMd.regular,
                color = Palette.blackHigh,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            painter = Images.Icons.trash(),
            contentDescription = "Remove link",
            tint = Palette.cardBgRed,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .clickable { onRemove() }
                .padding(8.dp)
        )
    }
}

@Composable
private fun AddButton(addTitle: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Palette.white)
            .border(0.5.dp, Palette.graySecondary, CircleShape)
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = addTitle,
            style = AppTypography.BodyTextMd.regular,
            color = Palette.blackHigh,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = Images.Icons.plus(),
            contentDescription = null,
            tint = Palette.blackHigh
        )
    }
}
