package com.bonjur.designSystem.components.categorieChips

import com.bonjur.designSystem.ui.theme.colors.Palette
import java.util.UUID
import androidx.compose.ui.graphics.Color

data class CategoriesChipModel(
    val id: Int,
    val title: String,
    val selected: Boolean
) {
    val backgroundColor: Color
        get() = if (selected) Palette.greenLight else Palette.grayQuaternary

    val borderColor: Color
        get() = if (selected) Palette.green900 else Palette.grayTeritary
}