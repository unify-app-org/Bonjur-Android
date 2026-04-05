package com.bonjur.designSystem.ui.theme.Typography

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val AppMaterialTypography = Typography(
    displayLarge = AppTypography.TitleXL.extraBold,
    displayMedium = AppTypography.TitleXL.regular,
    displaySmall = AppTypography.TitleL.bold,

    headlineLarge = AppTypography.TitleL.bold,
    headlineMedium = AppTypography.TitleMd.bold,
    headlineSmall = AppTypography.TitleSm.bold,

    titleLarge = AppTypography.HeadingXL.bold,
    titleMedium = AppTypography.HeadingMd.semiBold,
    titleSmall = AppTypography.BodyTextMd.semiBold,

    bodyLarge = AppTypography.BodyTextMd.regular,
    bodyMedium = AppTypography.BodyTextSm.regular,
    bodySmall = AppTypography.TextL.regular,

    labelLarge = AppTypography.TextL.semiBold,
    labelMedium = AppTypography.TextMd.medium,
    labelSmall = AppTypography.TextSm.medium,
)

object AppTypography {

    // MARK: - Title Styles
    object TitleXL {
        val extraBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = TypographyTokens.FontSize.titleXL,
            lineHeight = TypographyTokens.LineHeight.titleXL,
            letterSpacing = TypographyTokens.LetterSpacing.titleXL
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.titleXL,
            lineHeight = TypographyTokens.LineHeight.titleXL,
            letterSpacing = TypographyTokens.LetterSpacing.titleXL
        )
    }

    object TitleL {
        val extraBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = TypographyTokens.FontSize.titleL,
            lineHeight = TypographyTokens.LineHeight.titleL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.titleL,
            lineHeight = TypographyTokens.LineHeight.titleL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.titleL,
            lineHeight = TypographyTokens.LineHeight.titleL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.titleL,
            lineHeight = TypographyTokens.LineHeight.titleL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.titleL,
            lineHeight = TypographyTokens.LineHeight.titleL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object TitleMd {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.titleMd,
            lineHeight = TypographyTokens.LineHeight.titleMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val extraBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.ExtraBold,
            fontSize = TypographyTokens.FontSize.titleMd,
            lineHeight = TypographyTokens.LineHeight.titleMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.titleMd,
            lineHeight = TypographyTokens.LineHeight.titleMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.titleMd,
            lineHeight = TypographyTokens.LineHeight.titleMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.titleMd,
            lineHeight = TypographyTokens.LineHeight.titleMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object TitleSm {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.titleSm,
            lineHeight = TypographyTokens.LineHeight.titleSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.titleSm,
            lineHeight = TypographyTokens.LineHeight.titleSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.titleSm,
            lineHeight = TypographyTokens.LineHeight.titleSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.titleSm,
            lineHeight = TypographyTokens.LineHeight.titleSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    // MARK: - Heading Styles
    object HeadingXL {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.headingXL,
            lineHeight = TypographyTokens.LineHeight.headingXL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.headingXL,
            lineHeight = TypographyTokens.LineHeight.headingXL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.headingXL,
            lineHeight = TypographyTokens.LineHeight.headingXL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.headingXL,
            lineHeight = TypographyTokens.LineHeight.headingXL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object HeadingMd {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.headingMd,
            lineHeight = TypographyTokens.LineHeight.headingMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.headingMd,
            lineHeight = TypographyTokens.LineHeight.headingMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.headingMd,
            lineHeight = TypographyTokens.LineHeight.headingMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.headingMd,
            lineHeight = TypographyTokens.LineHeight.headingMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    // MARK: - Body Text Styles
    object BodyTextMd {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.bodyTextMd,
            lineHeight = TypographyTokens.LineHeight.bodyTextMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.bodyTextMd,
            lineHeight = TypographyTokens.LineHeight.bodyTextMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.bodyTextMd,
            lineHeight = TypographyTokens.LineHeight.bodyTextMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.bodyTextMd,
            lineHeight = TypographyTokens.LineHeight.bodyTextMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object BodyTextSm {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.bodyTextSm,
            lineHeight = TypographyTokens.LineHeight.bodyTextSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.bodyTextSm,
            lineHeight = TypographyTokens.LineHeight.bodyTextSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.bodyTextSm,
            lineHeight = TypographyTokens.LineHeight.bodyTextSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.bodyTextSm,
            lineHeight = TypographyTokens.LineHeight.bodyTextSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    // MARK: - Text Styles
    object TextL {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.textL,
            lineHeight = TypographyTokens.LineHeight.textL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.textL,
            lineHeight = TypographyTokens.LineHeight.textL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.textL,
            lineHeight = TypographyTokens.LineHeight.textL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.textL,
            lineHeight = TypographyTokens.LineHeight.textL,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object TextMd {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.textMd,
            lineHeight = TypographyTokens.LineHeight.textMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.textMd,
            lineHeight = TypographyTokens.LineHeight.textMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.textMd,
            lineHeight = TypographyTokens.LineHeight.textMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.textMd,
            lineHeight = TypographyTokens.LineHeight.textMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object TextSm {
        val bold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Bold,
            fontSize = TypographyTokens.FontSize.textSm,
            lineHeight = TypographyTokens.LineHeight.textSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val semiBold = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = TypographyTokens.FontSize.textSm,
            lineHeight = TypographyTokens.LineHeight.textSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.textSm,
            lineHeight = TypographyTokens.LineHeight.textSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.textSm,
            lineHeight = TypographyTokens.LineHeight.textSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    // MARK: - Caption Styles
    object CaptionMd {
        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.captionMd,
            lineHeight = TypographyTokens.LineHeight.captionMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.captionMd,
            lineHeight = TypographyTokens.LineHeight.captionMd,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }

    object CaptionSm {
        val medium = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Medium,
            fontSize = TypographyTokens.FontSize.captionSm,
            lineHeight = TypographyTokens.LineHeight.captionSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )

        val regular = TextStyle(
            fontFamily = Manrope,
            fontWeight = FontWeight.Normal,
            fontSize = TypographyTokens.FontSize.captionSm,
            lineHeight = TypographyTokens.LineHeight.captionSm,
            letterSpacing = TypographyTokens.LetterSpacing.none
        )
    }
}