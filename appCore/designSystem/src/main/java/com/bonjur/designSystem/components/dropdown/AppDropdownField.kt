package com.bonjur.designSystem.components.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.components.bottomSheet.AppBottomSheet
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette
import com.bonjur.designSystem.ui.theme.image.Images

data class AppDropdownOption(
    val id: String,
    val title: String
)

/**
 * Capsule dropdown that opens a bottom-sheet option list. Mirrors iOS `AppDropdownField`.
 */
@Composable
fun AppDropdownField(
    selection: AppDropdownOption?,
    options: List<AppDropdownOption>,
    placeholder: String,
    sheetTitle: String,
    onSelect: (AppDropdownOption) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null
) {
    var sheetOpen by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        title?.let {
            Text(text = it, style = AppTypography.HeadingMd.medium, color = Palette.blackHigh)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .border(0.5.dp, Palette.graySecondary, CircleShape)
                .clickable { sheetOpen = true }
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selection?.title ?: placeholder,
                style = AppTypography.BodyTextMd.regular,
                color = if (selection == null) Palette.grayPrimary else Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = Images.Icons.chevronDown02(),
                contentDescription = null,
                tint = Palette.blackMedium
            )
        }
    }

    if (sheetOpen) {
        AppBottomSheet(onDismiss = { sheetOpen = false }) {
            DropdownSheetContent(
                title = sheetTitle,
                options = options,
                selection = selection,
                onSelect = {
                    onSelect(it)
                    sheetOpen = false
                },
                onClose = { sheetOpen = false }
            )
        }
    }
}

@Composable
private fun DropdownSheetContent(
    title: String,
    options: List<AppDropdownOption>,
    selection: AppDropdownOption?,
    onSelect: (AppDropdownOption) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                style = AppTypography.TitleL.extraBold,
                color = Palette.blackHigh,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = Images.Icons.xmark(),
                contentDescription = "Close",
                tint = Palette.black,
                modifier = Modifier.clickable { onClose() }
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val selected = selection?.id == option.id
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(if (selected) Palette.grayQuaternary else Color.Transparent)
                        .clickable { onSelect(option) }
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option.title,
                        style = AppTypography.BodyTextMd.regular,
                        color = Palette.blackHigh,
                        modifier = Modifier.weight(1f)
                    )
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(Palette.blackHigh, CircleShape)
                        )
                    }
                }
            }
        }
    }
}
