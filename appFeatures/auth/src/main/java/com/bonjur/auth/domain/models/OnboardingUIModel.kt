package com.bonjur.auth.domain.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

data class OnboardingUIModel(
    val id: String,
    val title: String,
    val subtitle: String,
    val image: @Composable () -> Painter
)