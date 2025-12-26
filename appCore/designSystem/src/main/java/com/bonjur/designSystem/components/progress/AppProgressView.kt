package com.bonjur.designSystem.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette
import androidx.compose.runtime.getValue

@Composable
fun AppProgressView(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    val targetProgress = (currentStep.toFloat() / totalSteps.toFloat())
        .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        label = "ProgressAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Palette.grayTeritary.copy(alpha = 0.25f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(999.dp))
                .background(Palette.primary)
        )
    }
}
