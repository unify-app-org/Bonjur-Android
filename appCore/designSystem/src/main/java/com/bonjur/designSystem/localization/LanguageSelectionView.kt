package com.bonjur.designSystem.localization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bonjur.designsystem.R
import com.bonjur.designSystem.ui.theme.Typography.AppTypography
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Shared language picker: lists the supported app languages and reports the
 * chosen one back to the caller, which performs the switch. Used by Profile
 * settings and by the auth onboarding flow. Mirrors iOS `LanguageSelectionView`.
 */
@Composable
fun LanguageSelectionView(
    modifier: Modifier = Modifier,
    onSelect: (AppLanguage) -> Unit
) {
    val selected = LanguageManager.language

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Palette.white)
    ) {
        Text(
            text = stringResource(R.string.language_title),
            style = AppTypography.TitleSm.semiBold,
            color = Palette.black,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 16.dp)
        )

        AppLanguage.entries.forEachIndexed { index, language ->
            LanguageRow(
                language = language,
                isSelected = language == selected,
                onClick = { onSelect(language) }
            )
            if (index != AppLanguage.entries.lastIndex) {
                HorizontalDivider(
                    color = Palette.grayQuaternary,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    language: AppLanguage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = language.flag, fontSize = 28.sp)

        Text(
            text = language.title,
            style = AppTypography.TextL.regular,
            color = Palette.black,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .size(22.dp)
                .border(
                    1.dp,
                    if (isSelected) Palette.black else Palette.graySecondary,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Palette.black, CircleShape)
                )
            }
        }
    }
}
