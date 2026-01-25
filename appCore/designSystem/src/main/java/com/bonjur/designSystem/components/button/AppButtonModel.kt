package com.bonjur.designSystem.components.button

import androidx.compose.ui.graphics.Color
import com.bonjur.designSystem.ui.theme.colors.Palette

data class AppButtonModel(
    val type: ButtonType = ButtonType.Primary,
    val style: ButtonStyle = ButtonStyle.Default,
    val contentSize: ContentSize = ContentSize.Fit,
    val size: AppButtonSize = AppButtonSize.Large
) {
    val backgroundColor: Color
        get() = when (type) {
            ButtonType.Primary -> {
                if (style == ButtonStyle.Default) Palette.primary
                else Palette.secondary
            }
            ButtonType.Secondary -> {
                if (style == ButtonStyle.Default) Color.Transparent
                else Palette.primary.copy(alpha = 0.2f)
            }
            ButtonType.Tertiary -> {
                if (style == ButtonStyle.Default) Color.Transparent
                else Palette.black.copy(alpha = 0.05f)
            }
        }

    val horizontalPadding: Float = 23f

    val verticalPadding: Float
        get() = when (size) {
            AppButtonSize.Large -> 16f
            AppButtonSize.Medium -> 12f
            AppButtonSize.Small -> 10f
        }

    val foregroundColor: Color = Palette.black

    val borderColor: Color
        get() = if (style == ButtonStyle.Default) {
            Palette.blackHigh
        } else {
            Palette.border
        }

    val borderWidth: Float
        get() = when (type) {
            ButtonType.Secondary -> 0.5f
            else -> 0f
        }
}

enum class AppButtonSize {
    Large,
    Medium,
    Small
}

enum class ContentSize {
    Fill,
    Fit
}

enum class ButtonType {
    Primary,
    Secondary,
    Tertiary
}

enum class ButtonStyle {
    Default,
    Hover
}