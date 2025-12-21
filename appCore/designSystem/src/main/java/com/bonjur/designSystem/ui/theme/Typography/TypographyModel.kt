package com.bonjur.designSystem.ui.theme.Typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.bonjur.designsystem.R

val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold)
)

// MARK: - Tokens
object TypographyTokens {

    // Font Sizes
    object FontSize {
        val titleXL: TextUnit = 44.sp
        val titleL: TextUnit = 28.sp
        val titleMd: TextUnit = 22.sp
        val titleSm: TextUnit = 20.sp

        val headingXL: TextUnit = 18.sp
        val headingMd: TextUnit = 17.sp

        val bodyTextMd: TextUnit = 16.sp
        val bodyTextSm: TextUnit = 15.sp

        val textL: TextUnit = 14.sp
        val textMd: TextUnit = 13.sp
        val textSm: TextUnit = 12.sp

        val captionMd: TextUnit = 11.sp
        val captionSm: TextUnit = 10.sp
    }

    // Line Heights
    object LineHeight {
        val titleXL: TextUnit = 48.sp
        val titleL: TextUnit = 36.sp
        val titleMd: TextUnit = 28.sp
        val titleSm: TextUnit = 26.sp

        val headingXL: TextUnit = 24.sp
        val headingMd: TextUnit = 22.sp

        val bodyTextMd: TextUnit = 22.sp
        val bodyTextSm: TextUnit = 20.sp

        val textL: TextUnit = 18.sp
        val textMd: TextUnit = 17.sp
        val textSm: TextUnit = 16.sp

        val captionMd: TextUnit = 14.sp
        val captionSm: TextUnit = 13.sp
    }

    object LetterSpacing {
        val titleXL: TextUnit = (-0.088).sp
        val none: TextUnit = 0.sp
    }
}