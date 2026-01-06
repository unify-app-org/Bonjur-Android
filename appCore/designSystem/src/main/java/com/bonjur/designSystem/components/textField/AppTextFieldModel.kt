package com.bonjur.designSystem.components.textField

import androidx.compose.ui.text.input.KeyboardType

data class AppTextFieldModel(
    val title: String? = null,
    val type: FieldType = FieldType.Normal,
    val keyboardType: KeyboardType = KeyboardType.Text
)

enum class FieldType {
    Secure,
    Normal
}