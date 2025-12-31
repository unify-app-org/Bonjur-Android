package com.bonjur.designSystem.components.selectableList

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette
import androidx.compose.ui.graphics.Color

data class SelectableListItemModel(
    val id: Int,
    val title: String,
    val selected: Boolean,
    val style: Style = Style.Default
) {
    enum class Style {
        Default, MultiSelect
    }

    val backgroundColor: Color
        get() = if (selected) Palette.primary else Color.Transparent

    val borderColor: Color
        get() = Palette.grayPrimary

    val borderWidth: Dp
        get() = 0.5.dp
}
