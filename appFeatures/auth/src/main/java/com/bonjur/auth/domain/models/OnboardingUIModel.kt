package com.bonjur.auth.domain.models

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

/**
 * Slide copy is carried as string resources, not literals: the text is resolved
 * in composition, so switching the app language re-renders it in place. (iOS hit
 * the same trap — the copy lived in the use case and stayed English on switch.)
 */
data class OnboardingUIModel(
    val id: String,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
    /** Optional argument for a formatted title, e.g. the user's name on Welcome. */
    val titleArg: String? = null,
    val image: @Composable () -> Painter
)
