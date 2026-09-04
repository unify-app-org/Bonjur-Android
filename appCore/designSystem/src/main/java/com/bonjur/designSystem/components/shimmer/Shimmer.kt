//
//  Shimmer.kt
//  DesignSystem
//
//  Created by Huseyn Hasanov on 04.09.26
//

package com.bonjur.designSystem.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.bonjur.designSystem.ui.theme.colors.Palette

/**
 * Skeleton placeholders with a sweeping highlight — the "content is coming" state
 * for screens that own their whole layout, as opposed to [AppLoadingUI], which is a
 * blocking overlay for actions the user must wait out.
 *
 * Build one brush per screen and pass it to every block so the whole skeleton
 * pulses in phase; a per-block `rememberInfiniteTransition` gives each piece its
 * own clock and the screen flickers.
 *
 *     val brush = rememberShimmerBrush()
 *     ShimmerBox(Modifier.size(120.dp, 20.dp), brush = brush)
 *
 * Mirrors iOS `ShimmerBox` / `.shimmering()`.
 */
@Composable
fun rememberShimmerBrush(
    baseColor: Color = Palette.onBackground,
    highlightColor: Color = Palette.white.copy(alpha = 0.9f)
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SWEEP_MILLIS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerProgress"
    )

    // The gradient is placed in each block's own coordinate space, so the band is
    // deliberately wider than a card: the highlight travels all the way through
    // instead of popping in at one edge and vanishing at the other.
    val offset = progress * (BAND_PX * 2f) - BAND_PX
    return Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(offset, 0f),
        end = Offset(offset + BAND_PX, BAND_PX)
    )
}

/** One skeleton block. Size it with [modifier]; [shape] should match what it stands in for. */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    brush: Brush = rememberShimmerBrush()
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

private const val SWEEP_MILLIS = 1300
private const val BAND_PX = 420f
